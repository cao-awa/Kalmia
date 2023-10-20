package com.github.cao.awa.kalmia.session.database;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.database.provider.DatabaseProvider;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SessionDatabase {
    private static final byte[] ROOT = new byte[]{- 1};
    private final KeyValueDatabase database;

    public SessionDatabase(String path) throws Exception {
        this.database = DatabaseProvider.kv(path);
    }

    public void operation(BiConsumer<Long, Session> action) {
        byte[] seqByte = this.database.get(
                ROOT
        );

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq,
                              get(SkippedBase256.longToBuf(seq))
                );
            }
        }
    }

    public long nextSeq() {
        return SkippedBase256.readLong(BytesReader.of(this.database.get(ROOT))) + 1;
    }

    @Nullable
    public Session get(byte[] sid) {
        byte[] bytes = this.database.get(sid);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return Session.create(bytes);
    }

    public void delete(byte[] sid) {
        this.database.put(sid,
                          BytesUtil.EMPTY
        );
    }

    public void seqAll(Consumer<Long> action) {
        byte[] seqByte = this.database.get(ROOT);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq);
            }
        }
    }

    public void deleteAll() {
        seqAll(
                seq -> delete(SkippedBase256.longToBuf(seq))
        );
    }

    public long add(Session session) {
        byte[] seqByte = this.database.get(ROOT);

        long seq = seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));

        long nextSeq = seq + 1;

        byte[] nextSeqByte = SkippedBase256.longToBuf(nextSeq);

        this.database.put(nextSeqByte,
                          session.toBytes()
        );

        this.database.put(ROOT,
                          nextSeqByte
        );

        return nextSeq;
    }

    public void set(byte[] seq, Session session) {
        this.database.put(seq,
                          session.toBytes()
        );
    }
}
