package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.digest.MessageDigest;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class PlainMessage extends Message {
    private final long sender;
    private final String msg;
    private final MessageDigest digest;

    public PlainMessage(String msg, long sender, MessageDigest digest) {
        this.msg = msg;
        this.sender = sender;
        this.digest = digest;
    }

    public String getMsg() {
        return this.msg;
    }

    public long getSender() {
        return sender;
    }

    public PlainMessage(String msg, long sender) {
        this.msg = msg;
        this.sender = sender;
        this.digest = new MessageDigest(MessageDigger.Sha3.SHA_512.instanceName(),
                                        Mathematics.toBytes(MessageDigger.digest(msg,
                                                                                 MessageDigger.Sha3.SHA_512
                                                            ),
                                                            16
                                        )
        );
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{0},
                                SkippedBase256.longToBuf(this.sender),
                                Base256.tagToBuf(this.msg.length()),
                                this.msg.getBytes(StandardCharsets.UTF_8),
                                new byte[]{(byte) this.digest.type()
                                                             .length()},
                                this.digest.type()
                                           .getBytes(StandardCharsets.UTF_8),
                                Base256.tagToBuf(this.digest.value()
                                                         .length),
                                this.digest.value()
        );
    }

    public static PlainMessage create(BytesReader reader) {
        if (reader.read() == 0) {
            String msg;
            String type;
            byte[] digest;

            long sender = SkippedBase256.readLong(reader);

            int msgLength = Base256.tagFromBuf(reader.read(2));
            msg = new String(reader.read(msgLength),
                             StandardCharsets.UTF_8
            );

            int typeLength = reader.read();
            type = new String(reader.read(typeLength),
                              StandardCharsets.UTF_8
            );

            int digestLength = Base256.tagFromBuf(reader.read(2));
            digest = reader.read(digestLength);

            return new PlainMessage(msg,
                                    sender,
                                    new MessageDigest(type,
                                                      digest
                                    )
            );
        } else {
            return null;
        }
    }

    @Override
    public MessageDigest getDigest() {
        return this.digest;
    }
}
