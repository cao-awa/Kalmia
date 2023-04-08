package com.github.cao.awa.kalmia.user.manage;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.kalmia.user.database.UserDatabase;

import java.io.IOException;
import java.util.function.BiConsumer;

public class UserManager {
    private final UserDatabase database;

    public UserManager(String path) throws IOException {
        this.database = new UserDatabase(path);
    }

    public synchronized long add(User user) {
        return this.database.add(user);
    }

    public synchronized long delete(long seq) {
        this.database.delete(Base256.longToBuf(seq));
        return seq;
    }

    public synchronized User get(long seq) {
        return this.database.get(Base256.longToBuf(seq));
    }

    public synchronized void operation( BiConsumer<Long, User> action) {
        this.database.operation(action);
    }

    public synchronized void deleteAll() {
        this.database.deleteAll();
    }
}
