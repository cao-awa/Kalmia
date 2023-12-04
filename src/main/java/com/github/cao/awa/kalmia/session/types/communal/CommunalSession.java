package com.github.cao.awa.kalmia.session.types.communal;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class CommunalSession extends Session {
    public CommunalSession(long sessionId) {
        super(sessionId);
    }

    public static CommunalSession create(BytesReader reader) {
        if (reader.read() == 2) {
            return new CommunalSession(SkippedBase256.readLong(reader));
        }
        return null;
    }

    @Override
    public byte[] bytes() {
        return BytesUtil.concat(new byte[]{2},
                                SkippedBase256.longToBuf(sessionId())
        );
    }

    @Override
    public boolean accessible(long userId) {
        return Kalmia.SERVER.sessionManager()
                            .accessibleChat(
                                    sessionId(),
                                    userId
                            )
                            .accessibleChat(false);
    }
}
