package com.github.cao.awa.kalmia.keypair.manager;

import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.database.KeypairDatabase;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import org.jetbrains.annotations.Nullable;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.function.BiConsumer;

public class KeypairManager {
    private final KeypairDatabase database;

    public KeypairManager(String path) throws Exception {
        this.database = new KeypairDatabase(path);
    }

    public PureExtraIdentity add(KeyPairStore store) {
        return this.database.add(store);
    }

    public void set(PureExtraIdentity identity, KeyPairStore store) {
        this.database.set(identity.extras(),
                          store
        );
    }

    public void delete(PureExtraIdentity identity) {
        this.database.remove(identity.extras());
    }

    @Nullable
    public KeyPairStore get(PureExtraIdentity identity) {
        return this.database.get(identity.extras());
    }

    public KeyPairStore getStore(PureExtraIdentity identity) {
        return this.database.createStore(identity.extras());
    }

    public void operation(BiConsumer<PureExtraIdentity, KeyPairStore> action) {
        this.database.operation(action);
    }

    public void deleteAll() {
        this.database.deleteAll();
    }

    public PublicKey publicKey(PureExtraIdentity identity) {
        return this.database.publicKey(identity.extras());
    }

    public void publicKey(PureExtraIdentity identity, PublicKey publicKey) {
        KeyPairStore store = this.database.createStore(identity.extras());

        KeyStore<? extends PrivateKey> privateKey = store.privateKey();

        this.database.putPublic(identity.extras(),
                                KeyStoreIdentity.createKeyPairStore(
                                                        identity,
                                                        publicKey,
                                                        privateKey == null ? BytesUtil.EMPTY : privateKey
                                                                .key()
                                                )
                                                .publicKey()
        );
    }

    public byte[] privateKey(PureExtraIdentity identity) {
        return this.database.privateKey(identity.extras());
    }

    public void privateKey(PureExtraIdentity identity, KeyStore<? extends PrivateKey> privateKey) {
        this.database.putPrivate(identity.extras(),
                                 privateKey
        );
    }
}
