package com.github.cao.awa.kalmia.session.communal;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.group.GroupSession;

import java.nio.charset.StandardCharsets;

public class CommunalSession extends GroupSession {
    private static final byte[] HEADER = new byte[]{2};

    public CommunalSession(long sessionId, String displayName, long subscriberCount) {
        super(sessionId,
              displayName,
              subscriberCount
        );
    }

    public static CommunalSession create(BytesReader reader) {
        if (reader.read() == 2) {
            return new CommunalSession(
                    SkippedBase256.readLong(reader),
                    new String(reader.read(Base256.readTag(reader)),
                               StandardCharsets.UTF_8
                    ),
                    SkippedBase256.readLong(reader)
            );
        }
        return null;
    }

    @Override
    public boolean accessible(long userId) {
        return Kalmia.SERVER.sessionManager()
                            .accessible(
                                    sessionId(),
                                    userId
                            )
                            .accessibleChat(false);
    }

    @Override
    public byte[] header() {
        return HEADER;
    }
}
