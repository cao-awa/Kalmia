package com.github.cao.awa.kalmia.user.database;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotation.number.encode.ShouldSkipped;
import com.github.cao.awa.kalmia.database.DatabaseProvide;
import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.DeletedUser;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UserDatabase {
    private static final byte[] ROOT = new byte[]{- 2};
    private static final byte[] SESSION_DELIMITER = new byte[]{- 3};
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
    public User get(@ShouldSkipped byte[] uid) {
        byte[] userBytes = this.database.get(uid);
        if (userBytes == null || userBytes.length == 0) {
            return null;
        }
        return User.create(userBytes);
    }

    public void delete(@ShouldSkipped byte[] uid) {
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

    public void set(@ShouldSkipped byte[] seq, User user) {
        this.database.put(seq,
                          user.toBytes()
        );
    }

    public byte[] session(@ShouldSkipped byte[] currentSeq, @ShouldSkipped byte[] targetSeq) {
        return this.database.get(BytesUtil.concat(currentSeq,
                                                  SESSION_DELIMITER,
                                                  targetSeq
        ));
    }

    public void session(@ShouldSkipped byte[] currentSeq, @ShouldSkipped byte[] targetSeq, byte[] sessionId) {
        this.database.put(BytesUtil.concat(currentSeq,
                                           SESSION_DELIMITER,
                                           targetSeq
                          ),
                          sessionId
        );
    }
}
