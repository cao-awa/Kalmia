package com.github.cao.awa.kalmia.session.types.group;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class GroupSession extends Session {
    private static final byte[] HEADER = new byte[]{3};

    public GroupSession(long sessionId) {
        super(sessionId);
    }

    public static GroupSession create(BytesReader reader) {
        if (reader.read() == 3) {
            return new GroupSession(
                    SkippedBase256.readLong(reader)
            );
        }
        return null;
    }

    @Override
    public byte[] bytes() {
        return BytesUtil.concat(header(),
                                SkippedBase256.longToBuf(sessionId())
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
}
