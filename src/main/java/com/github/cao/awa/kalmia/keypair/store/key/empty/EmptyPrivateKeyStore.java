package com.github.cao.awa.kalmia.keypair.store.key.empty;

import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;

import java.security.interfaces.ECPrivateKey;

public class EmptyPrivateKeyStore extends KeyStore<ECPrivateKey> {
    private final KeyPairStore store;
    private final byte[] prikey;

    public EmptyPrivateKeyStore(KeyPairStore store, byte[] prikey) {
        this.store = store;
        this.prikey = prikey;
    }

    @Override
    public ECPrivateKey decode(boolean requireFailure) {
        return null;
    }

    @Override
    public KeyPairStore keypairStore() {
        return this.store;
    }

    @Override
    public byte[] key() {
        return this.prikey;
    }
}
