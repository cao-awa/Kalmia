package com.github.cao.awa.kalmia.session.manage;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.kalmia.session.database.SessionDatabase;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class SessionManager {
    private final SessionDatabase database;

    public SessionManager(String path) throws Exception {
        this.database = new SessionDatabase(path);
    }

    public synchronized long add(Session session) {
        return this.database.add(session);
    }

    public synchronized void set(long seq, Session session) {
        this.database.set(SkippedBase256.longToBuf(seq),
                          session
        );
    }

    public synchronized long delete(long seq) {
        this.database.delete(SkippedBase256.longToBuf(seq));
        return seq;
    }

    @Nullable
    public synchronized Session session(long seq) {
        return this.database.get(SkippedBase256.longToBuf(seq));
    }

    public synchronized void operation(BiConsumer<Long, Session> action) {
        this.database.operation(action);
    }

    public synchronized void deleteAll() {
        this.database.deleteAll();
    }

    public synchronized boolean accessible(long targetId, long sessionId) {
        Session session = this.database.get(SkippedBase256.longToBuf(sessionId));
        return session != null && session.accessible(targetId);
    }

    public synchronized long nextSeq() {
        return this.database.nextSeq();
    }
}
