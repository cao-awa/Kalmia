package com.github.cao.awa.kalmia.message.manager;

import com.github.cao.awa.kalmia.database.key.BytesKey;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.database.message.MessageDatabase;

import java.util.function.BiConsumer;

public class MessageManager {
    private final MessageDatabase database;

    public MessageManager(String path) throws Exception {
        this.database = new MessageDatabase(path);
    }

    public long send(PureExtraIdentity sessionIdentity, Message msg) {
        return this.database.send(sessionIdentity, msg);
    }

    public long delete(PureExtraIdentity sessionIdentity, long seq) {
        this.database.markDelete(sessionIdentity, BytesKey.of(SkippedBase256.longToBuf(seq)));
        return seq;
    }

    public Message get(PureExtraIdentity sessionIdentity, long seq) {
        return this.database.get(sessionIdentity, BytesKey.of(SkippedBase256.longToBuf(seq)));
    }

    public Message get(LongAndExtraIdentity identity) {
        return this.database.get(identity);
    }

    public void operation(PureExtraIdentity sessionIdentity, BiConsumer<Long, Message> action) {
        this.database.operation(sessionIdentity, action);
    }

    public void operation(PureExtraIdentity sessionIdentity, long from, long to, BiConsumer<Long, Message> action) {
        this.database.operation(sessionIdentity, from, to, action);
    }

    public long seq(PureExtraIdentity sessionIdentity) {
        return this.database.curSeq(sessionIdentity);
    }

    public long nextSeq(PureExtraIdentity sessionIdentity) {
        return this.database.nextSeq(sessionIdentity);
    }

    public void deleteAll(PureExtraIdentity sessionIdentity) {
        this.database.deleteAll(sessionIdentity);
    }

    public void set(PureExtraIdentity sessionIdentity, long seq, Message msg) {
        this.database.identity(sessionIdentity, BytesKey.of(SkippedBase256.longToBuf(seq)), msg.identity());

        this.database.set(msg.identity(), msg);
    }

    public void set(LongAndExtraIdentity identity, Message message) {
        this.database.set(BytesKey.of(identity.toBytes()), message);
    }

    public void seq(PureExtraIdentity sessionIdentity, long seq) {
        this.database.curSeq(sessionIdentity, SkippedBase256.longToBuf(seq));
    }
}
