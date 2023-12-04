package com.github.cao.awa.kalmia.message.plains;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.digest.DigestData;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class PlainsMessage extends Message {
    private static final byte[] HEADER = new byte[]{1};
    private long sender;
    private String msg;
    private DigestData digest;

    @Auto
    public PlainsMessage() {

    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    public PlainsMessage(String msg, long sender) {
        this.msg = msg;
        this.sender = sender;
        this.digest = new DigestData(MessageDigger.Sha3.SHA_512,
                                     Mathematics.toBytes(MessageDigger.digest(msg,
                                                                              MessageDigger.Sha3.SHA_512
                                                         ),
                                                         16
                                     )
        );
    }

    public PlainsMessage(byte[] gid, String msg, long sender) {
        super(gid);
        this.msg = msg;
        this.sender = sender;
        this.digest = new DigestData(MessageDigger.Sha3.SHA_512,
                                     Mathematics.toBytes(MessageDigger.digest(msg,
                                                                              MessageDigger.Sha3.SHA_512
                                                         ),
                                                         16
                                     )
        );
    }

    public static PlainsMessage create(BytesReader reader) {
        if (reader.read() == 1) {
            byte[] gid = reader.read(24);

            long sender = SkippedBase256.readLong(reader);

            int length = SkippedBase256.readInt(reader);

            String msg = new String(reader.read(length),
                                    StandardCharsets.UTF_8
            );

            return new PlainsMessage(gid,
                                     msg,
                                     sender
            );
        } else {
            return null;
        }
    }


    public String msg() {
        return this.msg;
    }

    public long sender() {
        return sender;
    }

    @Override
    public DigestData digest() {
        return this.digest;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(header(),
                                globalId(),
                                SkippedBase256.longToBuf(this.sender),
                                SkippedBase256.intToBuf(this.msg.length()),
                                this.msg.getBytes(StandardCharsets.UTF_8)
        );
    }
}
