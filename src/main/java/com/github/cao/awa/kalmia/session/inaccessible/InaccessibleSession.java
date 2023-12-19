package com.github.cao.awa.kalmia.session.inaccessible;

import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.session.Session;

public class InaccessibleSession extends Session {
    private static final PureExtraIdentity IDENTITY = PureExtraIdentity.create(BytesRandomIdentifier.create(16));
    private static final byte[] HEADER = new byte[]{- 1};

    public InaccessibleSession() {
        super(IDENTITY);
    }

    @Override
    public byte[] bytes() {
        return header();
    }

    @Override
    public boolean accessible(LongAndExtraIdentity accessIdentity) {
        return false;
    }

    @Override
    public byte[] header() {
        return HEADER;
    }

    @Override
    public String displayName() {
        return "Inaccessible session";
    }
}
