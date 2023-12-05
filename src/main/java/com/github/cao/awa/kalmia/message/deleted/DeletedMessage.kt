package com.github.cao.awa.kalmia.message.deleted;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.digest.DigestData;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DeletedMessage extends Message {
    private static final byte[] HEADER = new byte[]{- 1};
    private final long sender;
    private final DigestData digestData;

    public DeletedMessage(long sender, DigestData digestData) {
        this.sender = sender;
        this.digestData = digestData;
    }

    public DeletedMessage(byte[] globalId, long sender, DigestData digestData) {
        super(globalId);
        this.sender = sender;
        this.digestData = digestData;
    }

    public long sender() {
        return this.sender;
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(header(),
                                globalId(),
                                SkippedBase256.longToBuf(this.sender),
                                this.digestData.serialize()
        );
    }

    public static DeletedMessage create(BytesReader reader) {
        if (reader.read() == - 1) {
            byte[] gid = reader.read(24);

            long sender = SkippedBase256.readLong(reader);

            DigestData digestData = DigestData.create(reader);

            return new DeletedMessage(gid,
                                      sender,
                                      digestData
            );
        } else {
            return null;
        }
    }

    @Override
    public DigestData digest() {
        return this.digestData;
    }
}
