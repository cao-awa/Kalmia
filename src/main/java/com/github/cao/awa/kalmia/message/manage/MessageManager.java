package com.github.cao.awa.kalmia.message.manage;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.database.message.MessageDatabase;

import java.util.function.BiConsumer;

public class MessageManager {
    private final MessageDatabase database;

    public MessageManager(String path) throws Exception {
        this.database = new MessageDatabase(path);
    }

    public synchronized long send(long sid, Message msg) {
        return this.database.send(SkippedBase256.longToBuf(sid),
                                  msg
        );
    }

    public synchronized long delete(long sid, long seq) {
        this.database.markDelete(SkippedBase256.longToBuf(sid),
                                 SkippedBase256.longToBuf(seq)
        );
        return seq;
    }

    public synchronized Message get(long sid, long seq) {
        return this.database.get(SkippedBase256.longToBuf(sid),
                                 SkippedBase256.longToBuf(seq)
        );
    }

    public synchronized void operation(long sid, BiConsumer<Long, Message> action) {
        this.database.operation(SkippedBase256.longToBuf(sid),
                                action
        );
    }

    public synchronized void operation(long sid, long from, long to, BiConsumer<Long, Message> action) {
        this.database.operation(SkippedBase256.longToBuf(sid),
                                from,
                                to,
                                action
        );
    }

    public synchronized long seq(long sid) {
        return this.database.seq(SkippedBase256.longToBuf(sid));
    }

    public synchronized long curSeq(long sid) {
        return this.database.curSeq(SkippedBase256.longToBuf(sid));
    }

    public synchronized void deleteAll(long sid) {
        this.database.deleteAll(SkippedBase256.longToBuf(sid));
    }

    public void set(long sid, long seq, Message msg) {
        this.database.gid(
                SkippedBase256.longToBuf(sid),
                SkippedBase256.longToBuf(seq),
                msg.globalId()
        );

        this.database.put(msg.globalId(),
                          msg
        );
    }
}
