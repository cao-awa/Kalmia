package com.github.cao.awa.kalmia.user.manager;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.kalmia.user.database.UserDatabase;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class UserManager {
    private final UserDatabase database;

    public UserManager(String path) throws Exception {
        this.database = new UserDatabase(path);
    }

    public synchronized long add(User user) {
        return this.database.add(user);
    }

    public synchronized void set(long seq, User user) {
        this.database.set(SkippedBase256.longToBuf(seq),
                          user
        );
    }

    public synchronized long delete(long seq) {
        this.database.markUseless(SkippedBase256.longToBuf(seq));
        return seq;
    }

    @Nullable
    public synchronized User get(long seq) {
        return this.database.get(SkippedBase256.longToBuf(seq));
    }

    public synchronized void operation(BiConsumer<Long, User> action) {
        this.database.operation(action);
    }

    public synchronized void deleteAll() {
        this.database.uselessAll();
    }

    public synchronized long session(long self, long target) {
        byte[] sessionData = this.database.session(SkippedBase256.longToBuf(self),
                                                   SkippedBase256.longToBuf(target)
        );
        if (sessionData == null) {
            return - 1;
        }
        return SkippedBase256.readLong(BytesReader.of(sessionData));
    }

    public synchronized void session(long self, long target, long sessionId) {
        this.database.session(SkippedBase256.longToBuf(self),
                              SkippedBase256.longToBuf(target),
                              SkippedBase256.longToBuf(sessionId)
        );
    }

    public List<Long> sessionListeners(long uid) {
        List<Long> list = this.database.sessionListeners(SkippedBase256.longToBuf(uid));
        list.add(0L);
        return list;
    }

    public void sessionListeners(long uid, List<Long> listeners) {
        this.database.sessionListeners(SkippedBase256.longToBuf(uid),
                                       listeners
        );
    }

    public Set<Long> keyStores(long uid) {
        return this.database.keyStores(SkippedBase256.longToBuf(uid));
    }

    public void keyStores(long uid, Set<Long> stores) {
        this.database.keyStores(SkippedBase256.longToBuf(uid),
                                stores
        );
    }
}
