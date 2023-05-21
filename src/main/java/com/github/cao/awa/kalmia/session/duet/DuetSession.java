package com.github.cao.awa.kalmia.session.duet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DuetSession extends Session {
    private final long target1;
    private final long target2;

    public DuetSession(long target1, long target2) {
        this.target1 = target1;
        this.target2 = target2;
    }

    public static DuetSession create(BytesReader reader) {
        if (reader.read() == 1) {
            return new DuetSession(SkippedBase256.readLong(reader),
                                   SkippedBase256.readLong(reader)
            );
        }
        return null;
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{1},
                                SkippedBase256.longToBuf(this.target1),
                                SkippedBase256.longToBuf(this.target2)
        );
    }

    @Override
    public boolean accessible(long userId) {
        return this.target1 == userId || this.target2 == userId;
    }
}
