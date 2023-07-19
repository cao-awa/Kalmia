package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.attack.replay.ReplayAttack;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.count.TrafficCount;
import com.github.cao.awa.kalmia.network.encode.exception.ReplayAttackException;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class RequestDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER = LogManager.getLogger("RequestDecoder");
    private final RequestRouter router;
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private long lengthMarker = 0;
    private long currentLength = 0;
    private boolean usingB36 = false;

    public RequestDecoder(RequestRouter router) {
        this.router = router;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() > 0) {
            int dataLength;
            // Length marker is zero means the packet is first frame to here.
            // Need to update the marker.
            if (this.lengthMarker == 0) {
                // Compatible to base36 encoding.
                this.usingB36 = in.readChar() == 'b';

                if (this.usingB36) {
                    // Decode length by base36.
                    int lengthOfLengthMarker = Integer.parseInt(String.valueOf(in.readChar()));

                    byte[] lengthMarker = new byte[lengthOfLengthMarker];
                    in.readBytes(lengthMarker);

                    dataLength = SkippedBase256.readInt(new BytesReader(new BigInteger(new String(lengthMarker,
                                                                                                  StandardCharsets.US_ASCII
                    ),
                                                                                       36
                    ).toByteArray()));
                } else {
                    // Mark byte in SkippedBase256.
                    byte skipped = in.readByte();

                    // The mark is -1 means this number is not skipped, use the full Base256 decoding.
                    byte[] lengthMarker = new byte[skipped == - 1 ? 4 : skipped];
                    in.readBytes(lengthMarker);

                    // Let it skipped(full Base256 will auto decoded in here).
                    dataLength = SkippedBase256.readInt(new BytesReader(BytesUtil.concat(new byte[]{skipped},
                                                                                         lengthMarker
                    )));
                }

                // Mark the packet length.
                this.lengthMarker = dataLength;

                // Re length.
                dataLength = Math.min(in.readableBytes(),
                                      dataLength
                );
            } else {
                dataLength = in.readableBytes();
            }

            // Read the data of this packet frame.
            byte[] data = new byte[dataLength];
            in.readBytes(data);

            // Commit traffic count.
            TrafficCount.received(4 + data.length);

            // Write to buffer and update current length.
            this.output.write(data);

            this.currentLength += data.length;

            // The current length should equal to marker, then done this packet reading, process it.
            if (this.currentLength == this.lengthMarker) {
                // Processes...
                done(this.output.toByteArray(),
                     out
                );

                // Reset to prepare reading next packet.
                this.output.reset();
                this.lengthMarker = 0;
                this.currentLength = 0;
            }
        }
    }

    private void done(byte[] payload, List<Object> out) {
        if (this.usingB36) {
            payload = new BigInteger(new String(payload,
                                                StandardCharsets.US_ASCII
            ),
                                     36
            ).toByteArray();
        }

        // Build the reader.
        BytesReader reader = new BytesReader(payload);

        // Skip the FIRST REPLACEMENT for compatible to base 36 mode.
        reader.skip(1);

        // Decode data by router.
        reader.reset(this.router.decode(reader.all()));

        // Commit traffic count.
        TrafficCount.decoded(payload.length);

        byte[] digest = reader.read(reader.read());

        // Use to digest the completed packet data.
        reader.flag();
        byte[] remain = reader.all();
        reader.back();

        // Replay mark and timestamp used to prevent the replay attack.
        byte[] replayMark = reader.read(16);

        long timestamp = SkippedBase256.readLong(reader);

        // Check packet identity, ensure packet is not replay to server, prevent the replay attack.
        if (! ReplayAttack.valid(replayMark,
                                 timestamp
        )) {
            throw new ReplayAttackException();
        }

        // Read packet id, used to determine packet deserializer.
        long id = SkippedBase256.readLong(reader);

        // Read receipt identity.
        byte[] receipt = reader.read() == - 1 ? Packet.RECEIPT : Packet.read(reader);

        // If digest not equals then means the packet is incomplete or modified.
        if (! Arrays.equals(MessageDigger.digestToBytes(remain,
                                                        MessageDigger.Sha3.SHA_256
                            ),
                            digest
        )) {
            throw new InvalidPacketException("Packet are not completed");
        }

        // Create the packet and let it be processed in later handlers.
        out.add(UnsolvedPacketFactor.create(id,
                                            reader.all(),
                                            receipt
        ));
    }
}