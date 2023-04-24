package com.github.cao.awa.kalmia.user.database;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.database.DatabaseProvide;
import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.DeletedUser;
import com.github.cao.awa.kalmia.user.User;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UserDatabase {
    private static final byte[] ROOT = new byte[]{- 2};
    private final KeyValueDatabase database;

    public UserDatabase(String path) throws Exception {
        this.database = DatabaseProvide.kv(path);
    }

    public void operation(BiConsumer<Long, User> action) {
        byte[] seqByte = this.database.get(
                ROOT
        );

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq,
                              get(SkippedBase256.longToBuf(seq))
                );
            }
        }
    }

    @Nullable
    public User get(byte[] uid) {
        byte[] userBytes = this.database.get(uid);
        if (userBytes == null || userBytes.length == 0) {
            return null;
        }
        return User.create(userBytes);
    }

    public void delete(byte[] uid) {
        User source = get(uid);
        set(uid,
            new DeletedUser(TimeUtil.nano())
        );
    }

    public void seqAll(Consumer<Long> action) {
        byte[] seqByte = this.database.get(ROOT);

        long count = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

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

    public long add(User user) {
        byte[] seqByte = this.database.get(ROOT);

        long seq = seqByte == null ? - 1 : SkippedBase256.readLong(new BytesReader(seqByte));

        long nextSeq = seq + 1;

        byte[] nextSeqByte = SkippedBase256.longToBuf(nextSeq);

        this.database.put(nextSeqByte,
                          user.toBytes()
        );

        this.database.put(ROOT,
                          nextSeqByte
        );

        return nextSeq;
    }

    public void set(byte[] seq, User user) {
        this.database.put(seq,
                          user.toBytes()
        );
    }
}
