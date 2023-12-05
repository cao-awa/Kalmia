package com.github.cao.awa.kalmia.session.manager;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.kalmia.session.SessionAccessibleData;
import com.github.cao.awa.kalmia.session.database.SessionDatabase;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SessionManager {
    private final SessionDatabase database;

    public SessionManager(String path) throws Exception {
        this.database = new SessionDatabase(path);
    }

    public long add(Session session) {
        return this.database.add(session);
    }

    public void set(long seq, Session session) {
        this.database.put(SkippedBase256.longToBuf(seq),
                          session
        );
    }

    public long delete(long seq) {
        this.database.remove(SkippedBase256.longToBuf(seq));
        return seq;
    }

    @Nullable
    public Session session(long seq) {
        return this.database.get(SkippedBase256.longToBuf(seq));
    }

    public void operation(BiConsumer<Long, Session> action) {
        this.database.operation(action);
    }

    public void deleteAll() {
        this.database.deleteAll();
    }

    public void updateAccessible(long sessionId, long userId, Consumer<SessionAccessibleData> operation) {
        SessionAccessibleData data = this.database.accessible(
                SkippedBase256.longToBuf(sessionId),
                SkippedBase256.longToBuf(userId)
        );

        operation.accept(data);

        this.database.accessible(
                SkippedBase256.longToBuf(sessionId),
                SkippedBase256.longToBuf(userId),
                data
        );
    }

    public SessionAccessibleData accessible(long sessionId, long userId) {
        return this.database.accessible(
                SkippedBase256.longToBuf(sessionId),
                SkippedBase256.longToBuf(userId)
        );
    }

    public long nextSeq() {
        return this.database.nextSeq();
    }
}
