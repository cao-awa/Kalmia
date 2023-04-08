package com.github.cao.awa.kalmia.message.manage;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.database.message.MessageDatabase;

import java.io.IOException;
import java.util.function.BiConsumer;

public class MessageManager {
    private final MessageDatabase database;

    public MessageManager(String path) throws IOException {
        this.database = new MessageDatabase(path);
    }

    public synchronized long send(long sid, Message msg) {
        return this.database.send(Base256.longToBuf(sid), msg);
    }

    public synchronized long delete(long sid, long seq) {
        this.database.delete(Base256.longToBuf(sid), Base256.longToBuf(seq));
        return seq;
    }

    public synchronized Message get(long sid, long seq) {
        return this.database.get(Base256.longToBuf(sid), Base256.longToBuf(seq));
    }

    public synchronized void operation(long sid, BiConsumer<Long, Message> action) {
        this.database.operation(Base256.longToBuf(sid), action);
    }

    public synchronized void deleteAll(long sid) {
        this.database.deleteAll(Base256.longToBuf(sid));
    }
}
