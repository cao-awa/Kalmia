package com.github.cao.awa.kalmia.session.communal;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.group.GroupSession;

import java.nio.charset.StandardCharsets;

public class CommunalSession extends GroupSession {
    public static final PureExtraIdentity TEST_COMMUNAL_IDENTITY = PureExtraIdentity.create(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16});
    public static final CommunalSession TEST_COMMUNAL = new CommunalSession(TEST_COMMUNAL_IDENTITY,
                                                                            "Test public session",
                                                                            0
    );
    private static final byte[] HEADER = new byte[]{2};

    public CommunalSession(PureExtraIdentity sessionIdentity, String displayName, long subscriberCount) {
        super(sessionIdentity,
              displayName,
              subscriberCount
        );
    }

    public static CommunalSession create(BytesReader reader) {
        if (reader.read() == 2) {
            return new CommunalSession(
                    PureExtraIdentity.read(reader),
                    new String(reader.read(Base256.readTag(reader)),
                               StandardCharsets.UTF_8
                    ),
                    SkippedBase256.readLong(reader)
            );
        }
        return null;
    }

    @Override
    public boolean accessible(LongAndExtraIdentity accessIdentity) {
        return Kalmia.SERVER.sessionManager()
                            .accessible(
                                    identity(),
                                    accessIdentity
                            )
                            .accessibleChat(false);
    }

    @Override
    public byte[] header() {
        return HEADER;
    }
}
