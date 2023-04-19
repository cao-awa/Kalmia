package com.github.cao.awa.kalmia.chat.session.manager;

import com.github.cao.awa.apricot.anntation.Unsupported;
import com.github.cao.awa.kalmia.chat.session.database.SessionDatabase;

import java.io.IOException;

// TODO
@Unsupported
public class SessionManager {
    private final SessionDatabase database;

    public SessionManager(String path) throws IOException {
        this.database = new SessionDatabase(path);
    }
}
