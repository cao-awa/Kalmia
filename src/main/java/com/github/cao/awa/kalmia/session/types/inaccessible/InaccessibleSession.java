package com.github.cao.awa.kalmia.session.types.inaccessible;

import com.github.cao.awa.kalmia.session.Session;

public class InaccessibleSession extends Session {
    private static final byte[] HEADER = new byte[]{- 1};

    public InaccessibleSession() {
        super(- 1);
    }

    @Override
    public byte[] bytes() {
        return header();
    }

    @Override
    public boolean accessible(long userId) {
        return false;
    }

    @Override
    public byte[] header() {
        return HEADER;
    }
}
