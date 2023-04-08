package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DeletedUser extends User {
    private final long timestamp;

    public DeletedUser(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public static DeletedUser create(BytesReader reader) {
        if (reader.read() == -1) {
            long timestamp = SkippedBase256.readLong(reader);

            return new DeletedUser(timestamp);
        } else {
            return null;
        }
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{-1},
                                SkippedBase256.longToBuf(this.timestamp)
        );
    }
}
