package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.digest.MessageDigest;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class DeletedMessage extends Message {
    private final long sender;
    private final MessageDigest digest;

    public DeletedMessage(long sender, MessageDigest digest) {
        this.sender = sender;
        this.digest = digest;
    }

    public long getSender() {
        return this.sender;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{- 1},
                                SkippedBase256.longToBuf(this.sender),
                                new byte[]{(byte) this.digest.type()
                                                             .length()},
                                this.digest.type()
                                           .getBytes(StandardCharsets.UTF_8),
                                Base256.tagToBuf(this.digest.value()
                                                         .length),
                                this.digest.value()
        );
    }

    public static DeletedMessage create(BytesReader reader) {
        if (reader.read() == - 1) {
            String type;
            byte[] digest;

            long sender = SkippedBase256.readLong(reader);

            int typeLength = reader.read();
            type = new String(reader.read(typeLength),
                              StandardCharsets.UTF_8
            );

            int digestLength = Base256.tagFromBuf(reader.read(2));
            digest = reader.read(digestLength);

            return new DeletedMessage(sender,
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
