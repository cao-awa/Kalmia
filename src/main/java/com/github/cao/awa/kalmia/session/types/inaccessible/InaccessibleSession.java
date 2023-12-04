package com.github.cao.awa.kalmia.session.types.inaccessible;

import com.github.cao.awa.kalmia.session.Session;

public class InaccessibleSession extends Session {
    public InaccessibleSession() {
        super(- 1);
    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }

    @Override
    public boolean accessible(long userId) {
        return false;
    }
}
