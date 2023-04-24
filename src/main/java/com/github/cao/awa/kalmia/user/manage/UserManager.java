package com.github.cao.awa.kalmia.user.manage;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.kalmia.user.database.UserDatabase;
import org.jetbrains.annotations.Nullable;

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
        this.database.delete(SkippedBase256.longToBuf(seq));
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
        this.database.deleteAll();
    }
}
