package com.github.cao.awa.kalmia.session.types.group;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.session.Session;

public class GroupSession extends Session {
    public GroupSession(long sessionId) {
        super(sessionId);
    }

    @Override
    public byte[] bytes() {
        return new byte[0];
    }

    @Override
    public boolean accessible(long userId) {
        return Kalmia.SERVER.sessionManager()
                            .accessibleChat(
                                    sessionId(),
                                    userId
                            )
                            .accessibleChat(true);
    }
}
