package com.github.cao.awa.kalmia.session.types.duet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DuetSession extends Session {
    private static final byte[] HEADER = new byte[]{1};
    private final long target1;
    private final long target2;

    public DuetSession(long sessionId, long target1, long target2) {
        super(sessionId);
        this.target1 = target1;
        this.target2 = target2;
    }

    public static DuetSession create(BytesReader reader) {
        if (reader.read() == 1) {
            return new DuetSession(
                    SkippedBase256.readLong(reader),
                    SkippedBase256.readLong(reader),
                    SkippedBase256.readLong(reader)
            );
        }
        return null;
    }

    @Override
    public byte[] bytes() {
        return BytesUtil.concat(header(),
                                SkippedBase256.longToBuf(sessionId()),
                                SkippedBase256.longToBuf(this.target1),
                                SkippedBase256.longToBuf(this.target2)
        );
    }

    @Override
    public boolean accessible(long userId) {
        return Kalmia.SERVER.sessionManager()
                            .accessible(
                                    sessionId(),
                                    userId
                            )
                            .accessibleChat(true);
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    @Override
    public String displayName() {
        return "" + this.target1;
    }

    public long opposite(long userId) {
        if (userId == this.target1) {
            return this.target2;
        }
        if (userId == this.target2) {
            return this.target1;
        }
        return userId;
    }
}
