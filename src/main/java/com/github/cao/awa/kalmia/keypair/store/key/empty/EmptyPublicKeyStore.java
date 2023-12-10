package com.github.cao.awa.kalmia.keypair.store.key.empty;

import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;

import java.security.interfaces.ECPublicKey;

public class EmptyPublicKeyStore extends KeyStore<ECPublicKey> {
    private final KeyPairStore store;
    private final byte[] pubkey;

    public EmptyPublicKeyStore(KeyPairStore store, byte[] pubkey) {
        this.store = store;
        this.pubkey = pubkey;
    }

    @Override
    public ECPublicKey decode(boolean requireFailure) {
        return null;
    }

    @Override
    public KeyPairStore keypairStore() {
        return this.store;
    }

    @Override
    public byte[] key() {
        return this.pubkey;
    }
}
