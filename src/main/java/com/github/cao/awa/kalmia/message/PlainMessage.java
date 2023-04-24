package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.digest.MessageDigestData;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class PlainMessage extends Message {
    private final long sender;
    private final String msg;
    private final MessageDigestData digest;

    public PlainMessage(String msg, long sender, MessageDigestData digest) {
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
        this.digest = new MessageDigestData(MessageDigger.Sha3.SHA_512.instanceName(),
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
                                this.digest.toBytes()
        );
    }

    public static PlainMessage create(BytesReader reader) {
        if (reader.read() == 0) {
            String msg;

            long sender = SkippedBase256.readLong(reader);

            int msgLength = Base256.tagFromBuf(reader.read(2));
            msg = new String(reader.read(msgLength),
                             StandardCharsets.UTF_8
            );

            MessageDigestData digestData = MessageDigestData.create(reader);

            return new PlainMessage(msg,
                                    sender,
                                    digestData
            );
        } else {
            return null;
        }
    }

    @Override
    public MessageDigestData getDigest() {
        return this.digest;
    }
}
