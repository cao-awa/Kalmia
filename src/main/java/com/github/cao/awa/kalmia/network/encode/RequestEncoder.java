package com.github.cao.awa.kalmia.network.encode;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.count.TrafficCount;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RequestEncoder extends MessageToByteEncoder<Packet<?>> {
    private static final Logger LOGGER = LogManager.getLogger("RequestEncoder");
    private static final byte[] FIRST_REPLACEMENT = new byte[]{1};
    private final RequestRouter router;

    public RequestEncoder(RequestRouter router) {
        this.router = router;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet<?> request, ByteBuf out) throws Exception {
        // Encode it by router.
        byte[] payload = BytesUtil.concat(FIRST_REPLACEMENT,
                                          request.encode(this.router)
        );

        // Commit traffic count.
        TrafficCount.encoded(payload.length);

        // Mark the length for frame reading.
        byte[] lengthMark = Base256.intToBuf(payload.length);

        // Mark base36 mode.
        out.writeChar(this.router.shouldApplyBase36() ? 'b' : 'a');

        // Use the base36 to encode whole packet.
        if (this.router.shouldApplyBase36()) {
            payload = new BigInteger(payload).toString(36)
                                             .getBytes(StandardCharsets.US_ASCII);

            lengthMark = new BigInteger(SkippedBase256.intToBuf(payload.length)).toString(36)
                                                                                .getBytes(StandardCharsets.US_ASCII);

            out.writeChar(String.valueOf(lengthMark.length)
                                .charAt(0));
        }

        // Write packet length and payload data
        out.writeBytes(lengthMark);
        out.writeBytes(payload);

        // Commit traffic count.
        TrafficCount.sent(
                // Payload length.
                payload.length +
                        // Payload length mark length
                        lengthMark.length
        );
    }
}
