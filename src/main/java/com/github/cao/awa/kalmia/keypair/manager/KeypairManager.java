package com.github.cao.awa.kalmia.keypair.manager;

import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.database.KeypairDatabase;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.jetbrains.annotations.Nullable;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.function.BiConsumer;

public class KeypairManager {
    private final KeypairDatabase database;

    public KeypairManager(String path) throws Exception {
        this.database = new KeypairDatabase(path);
    }

    public synchronized long add(KeyPairStore store) {
        return this.database.add(store);
    }

    public synchronized void set(long seq, KeyPairStore user) {
        this.database.putPublic(SkippedBase256.longToBuf(seq),
                                user.publicKey()
        );
    }

    public synchronized long delete(long seq) {
        this.database.remove(SkippedBase256.longToBuf(seq));
        return seq;
    }

    @Nullable
    public synchronized KeyPair get(long seq) {
        return this.database.get(SkippedBase256.longToBuf(seq));
    }

    public synchronized void operation(BiConsumer<Long, KeyPair> action) {
        this.database.operation(action);
    }

    public synchronized void deleteAll() {
        this.database.deleteAll();
    }

    public synchronized PublicKey publicKey(long seq) {
        return this.database.publicKey(SkippedBase256.longToBuf(seq));
    }

    public synchronized void publicKey(long seq, PublicKey publicKey) {
        KeyPairStore store = this.database.createStore(SkippedBase256.longToBuf(seq));

        KeyStore<? extends PrivateKey> privateKey = store.privateKey();

        this.database.putPublic(SkippedBase256.longToBuf(seq),
                                KeyStoreIdentity.createKeyPairStore(
                                                        publicKey,
                                                        privateKey == null ? BytesUtil.EMPTY : privateKey
                                                                .key()
                                                )
                                                .publicKey()
        );
    }

    public byte[] privateKey(long seq) {
        return this.database.privateKey(SkippedBase256.longToBuf(seq));
    }

    public void privateKey(long seq, KeyStore<? extends PrivateKey> privateKey) {
        this.database.putPrivate(SkippedBase256.longToBuf(seq),
                                 privateKey
        );
    }
}
