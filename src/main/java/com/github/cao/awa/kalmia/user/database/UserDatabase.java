package com.github.cao.awa.kalmia.user.database;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.message.DeletedMessage;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.user.DeletedUser;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.github.cao.awa.viburnum.util.bytes.BytesUtil.concat;

public class UserDatabase {
    private final DB database;

    public UserDatabase(String path) throws IOException {
        this.database = new Iq80DBFactory().open(new File(path),
                                                 new Options().createIfMissing(true)
                                                              .writeBufferSize(1048560 * 16)
                                                              .compressionType(CompressionType.SNAPPY)
        );
    }

    public void operation(BiConsumer<Long, User> action) {
        byte[] seqByte = this.database.get(
                "/".getBytes()
        );

        long count = seqByte == null ? - 1 : Base256.longFromBuf(seqByte);

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq,
                              get(Base256.longToBuf(seq))
                );
            }
        }
    }

    public User get(byte[] uid) {
        byte[] userBytes = this.database.get(uid);
        return User.create(userBytes);
    }

    public void delete(byte[] uid) {
        User source = get(uid);
        this.database.put(uid,
                          new DeletedUser(TimeUtil.nano()).toBytes()
        );
    }

    public void seqAll(Consumer<Long> action) {
        byte[] seqByte = this.database.get("/".getBytes());

        long count = seqByte == null ? - 1 : Base256.longFromBuf(seqByte);

        count++;

        if (count > 0) {
            for (long seq = 0; seq < count; seq++) {
                action.accept(seq);
            }
        }
    }

    public void deleteAll() {
        seqAll(
                seq -> delete(Base256.longToBuf(seq))
        );
    }

    public long add(User user) {
        byte[] seqByte = this.database.get(
                "/".getBytes()
        );

        long seq = seqByte == null ? - 1 : Base256.longFromBuf(seqByte);

        long nextSeq = seq + 1;

        byte[] nextSeqByte = Base256.longToBuf(nextSeq);

        this.database.put(nextSeqByte,
                          user.toBytes()
        );

        this.database.put("/".getBytes(),
                          nextSeqByte
        );

        return nextSeq;
    }
}
