package com.github.cao.awa.kalmia.session.duet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class DuetSession extends Session {
    private static final byte[] HEADER = new byte[]{1};
    private final LongAndExtraIdentity target1;
    private final LongAndExtraIdentity target2;

    public DuetSession(PureExtraIdentity sessionIdentity, LongAndExtraIdentity target1, LongAndExtraIdentity target2) {
        super(sessionIdentity);
        this.target1 = target1;
        this.target2 = target2;
    }

    public static DuetSession create(BytesReader reader) {
        if (reader.read() == 1) {
            return new DuetSession(PureExtraIdentity.read(reader), LongAndExtraIdentity.read(reader), LongAndExtraIdentity.read(reader));
        }
        return null;
    }

    @Override
    public byte[] bytes() {
        return BytesUtil.concat(header(), identity().toBytes(), this.target1.toBytes(), this.target2.toBytes());
    }

    @Override
    public boolean accessible(LongAndExtraIdentity accessIdentity) {
        return Kalmia.SERVER.getSessionManager().accessible(identity(), accessIdentity).accessibleChat(true);
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    @Override
    public String displayName() {
        return "" + this.target1;
    }

    public LongAndExtraIdentity opposite(LongAndExtraIdentity userId) {
        if (userId.equals(this.target1)) {
            return this.target2;
        }
        if (userId.equals(this.target2)) {
            return this.target1;
        }
        return userId;
    }
}
