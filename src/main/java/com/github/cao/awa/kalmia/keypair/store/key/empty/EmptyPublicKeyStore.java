package com.github.cao.awa.kalmia.keypair.store.key.empty;

import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;

import java.security.interfaces.ECPublicKey;

public class EmptyPublicKeyStore extends KeyStore<ECPublicKey> {
    private final KeyPairStore store;
    private final byte[] prikey;

    public EmptyPublicKeyStore(KeyPairStore store, byte[] prikey) {
        this.store = store;
        this.prikey = prikey;
    }

    @Override
    public ECPublicKey decode(boolean requireFailure) {
        return null;
    }

    @Override
    public KeyPairStore keypairStone() {
        return this.store;
    }

    @Override
    public byte[] key() {
        return this.prikey;
    }
}
