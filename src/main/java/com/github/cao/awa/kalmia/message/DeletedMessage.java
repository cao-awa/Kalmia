package com.github.cao.awa.kalmia.message;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.digest.MessageDigestData;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DeletedMessage extends Message {
    private final long sender;
    private final MessageDigestData digestData;

    public DeletedMessage(long sender, MessageDigestData digestData) {
        this.sender = sender;
        this.digestData = digestData;
    }

    public long getSender() {
        return this.sender;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{- 1},
                                SkippedBase256.longToBuf(this.sender),
                                this.digestData.toBytes()
        );
    }

    public static DeletedMessage create(BytesReader reader) {
        if (reader.read() == - 1) {
            long sender = SkippedBase256.readLong(reader);

            MessageDigestData digestData = MessageDigestData.create(reader);

            return new DeletedMessage(sender,
                                      digestData
            );
        } else {
            return null;
        }
    }

    @Override
    public MessageDigestData getDigestData() {
        return this.digestData;
    }
}
