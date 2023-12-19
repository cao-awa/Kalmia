package com.github.cao.awa.kalmia.session.group;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

public class GroupSession extends Session {
    private static final byte[] HEADER = new byte[]{3};
    private final String displayName;
    private long subscriberCount;

    public GroupSession(PureExtraIdentity sessionIdentity, String displayName, long subscriberCount) {
        super(sessionIdentity);
        this.displayName = displayName;
        this.subscriberCount = subscriberCount;
    }

    public static GroupSession create(BytesReader reader) {
        if (reader.read() == 3) {
            return new GroupSession(
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
    public byte[] bytes() {
        return BytesUtil.concat(header(),
                                identity().toBytes(),
                                Base256.tagToBuf(this.displayName.length()),
                                this.displayName.getBytes(StandardCharsets.UTF_8),
                                SkippedBase256.longToBuf(this.subscriberCount)
        );
    }

    @Override
    public boolean accessible(LongAndExtraIdentity accessIdentity) {
        return Kalmia.SERVER.sessionManager()
                            .accessible(
                                    identity(),
                                    accessIdentity
                            )
                            .accessibleChat(true);
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    @Override
    public String displayName() {
        return this.displayName;
    }

    public long subscriberCount() {
        return this.subscriberCount;
    }

    public void subscriberCount(long count) {
        this.subscriberCount = count;
    }
}
