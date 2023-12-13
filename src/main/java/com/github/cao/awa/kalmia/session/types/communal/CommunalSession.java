package com.github.cao.awa.kalmia.session.types.communal;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class CommunalSession extends Session {
    private static final byte[] HEADER = new byte[]{2};
    private final String displayName;

    public CommunalSession(long sessionId, String displayName) {
        super(sessionId);
        this.displayName = displayName;
    }

    public static CommunalSession create(BytesReader reader) {
        if (reader.read() == 2) {
            return new CommunalSession(
                    SkippedBase256.readLong(reader),
                    new String(reader.read(Base256.readTag(reader)),
                               StandardCharsets.UTF_8
                    )
            );
        }
        return null;
    }

    @Override
    public byte[] bytes() {
        return BytesUtil.concat(header(),
                                SkippedBase256.longToBuf(sessionId()),
                                Base256.tagToBuf(this.displayName.length()),
                                this.displayName.getBytes(StandardCharsets.UTF_8)
        );
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

    @Override
    public String displayName() {
        return this.displayName;
    }
}
