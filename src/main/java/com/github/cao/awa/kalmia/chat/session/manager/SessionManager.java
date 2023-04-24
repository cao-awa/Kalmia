package com.github.cao.awa.kalmia.chat.session.manager;

import com.github.cao.awa.apricot.anntation.Unsupported;
import com.github.cao.awa.kalmia.chat.session.database.SessionDatabase;

// TODO
@Unsupported
public class SessionManager {
    private final SessionDatabase database;

    public SessionManager(String path) throws Exception {
        this.database = new SessionDatabase(path);
    }
}
