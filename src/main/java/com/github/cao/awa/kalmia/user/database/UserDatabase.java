package com.github.cao.awa.kalmia.user.database;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotations.number.encode.ShouldSkipped;
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase;
import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.database.provider.DatabaseProviders;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.UselessUser;
import com.github.cao.awa.kalmia.user.User;
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.jetbrains.annotations.Nullable;

import java.security.PublicKey;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class UserDatabase extends KeyValueDatabase<byte[], User> {
    private static final byte[] ROOT = new byte[]{1};
    private static final byte[] SESSION_DELIMITER = new byte[]{- 127};
    private static final byte[] PUBLIC_KEY_DELIMITER = new byte[]{127};
    private final KeyValueBytesDatabase delegate;

    public UserDatabase(String path) throws Exception {
        super(ApricotCollectionFactor :: hashMap);
        this.delegate = DatabaseProviders.bytes(path);
    }

    @Nullable
    public PublicKey publicKey(@ShouldSkipped byte[] uid) {
        BytesReader reader = BytesReader.of(this.delegate.get(BytesUtil.concat(uid,
                                                                               PUBLIC_KEY_DELIMITER
        )));
        if (reader.readable() > 1) {
            return PublicKeyIdentity.createKey(Base256.readTag(reader),
                                               reader.all()
            );
        } else {
            return null;
        }
    }

    public void publicKey(@ShouldSkipped byte[] uid, PublicKey publicKey) {
        this.delegate.put(BytesUtil.concat(uid,
                                           PUBLIC_KEY_DELIMITER
                          ),
                          PublicKeyIdentity.encodeKey(publicKey)
        );
    }

    public void operation(BiConsumer<Long, User> action) {
        long nextSeq = nextSeq();

        if (nextSeq > 0) {
            for (long seq = 0; seq < nextSeq; seq++) {
                action.accept(seq,
                              get(SkippedBase256.longToBuf(seq))
                );
            }
        }
    }

    @Nullable
    public User get(@ShouldSkipped byte[] uid) {
        return cache().get(
                uid,
                this :: getUser
        );
    }

    private User getUser(byte[] uid) {
        byte[] bytes = this.delegate.get(uid);
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return User.create(bytes);
    }

    @Override
    public void remove(byte[] uid) {
        cache().delete(
                uid,
                this.delegate :: remove
        );
    }

    public void markUseless(@ShouldSkipped byte[] uid) {
        User source = get(uid);
        put(uid,
            new UselessUser(TimeUtil.nano())
        );
    }

    public void seqAll(Consumer<Long> action) {
        long nextSeq = nextSeq();

        if (nextSeq > 0) {
            for (long seq = 0; seq < nextSeq; seq++) {
                action.accept(seq);
            }
        }
    }

    public void uselessAll() {
        seqAll(
                seq -> markUseless(SkippedBase256.longToBuf(seq))
        );
    }

    public long add(User user) {
        long nextSeq = nextSeq();

        byte[] nextSeqByte = SkippedBase256.longToBuf(nextSeq);

        this.delegate.put(nextSeqByte,
                          user.toBytes()
        );

        this.delegate.put(ROOT,
                          nextSeqByte
        );

        return nextSeq;
    }

    public long nextSeq() {
        byte[] seqByte = this.delegate.get(ROOT);

        long seq = seqByte == null ? - 1 : SkippedBase256.readLong(BytesReader.of(seqByte));

        return seq + 1;
    }

    public void put(@ShouldSkipped byte[] seq, User user) {
        this.delegate.put(seq,
                          user.toBytes()
        );

        if (user instanceof DefaultUser defaultUser) {
            publicKey(seq,
                      defaultUser.publicKey()
            );
        }
    }

    public byte[] session(@ShouldSkipped byte[] currentSeq, @ShouldSkipped byte[] targetSeq) {
        return this.delegate.get(BytesUtil.concat(currentSeq,
                                                  SESSION_DELIMITER,
                                                  targetSeq
        ));
    }

    public void session(@ShouldSkipped byte[] currentSeq, @ShouldSkipped byte[] targetSeq, @ShouldSkipped byte[] sessionId) {
        this.delegate.put(BytesUtil.concat(currentSeq,
                                           SESSION_DELIMITER,
                                           targetSeq
                          ),
                          sessionId
        );
    }
}
