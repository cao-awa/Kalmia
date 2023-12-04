package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class UselessUser extends User {
    private final long markTimestamp;

    public UselessUser(long markTimestamp) {
        this.markTimestamp = markTimestamp;
    }

    public long getMarkTimestamp() {
        return this.markTimestamp;
    }

    public static UselessUser create(BytesReader reader) {
        if (reader.read() == - 1) {
            long timestamp = SkippedBase256.readLong(reader);

            return new UselessUser(timestamp);
        } else {
            return null;
        }
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{-1},
                                SkippedBase256.longToBuf(this.markTimestamp)
        );
    }
}
