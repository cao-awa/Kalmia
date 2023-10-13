package com.github.cao.awa.kalmia.session.group;

import com.github.cao.awa.kalmia.session.Session;

public class GroupSession extends Session {
    @Override
    public byte[] toBytes() {
        return new byte[0];
    }

    @Override
    public boolean accessible(long userId) {
        return false;
    }
}
