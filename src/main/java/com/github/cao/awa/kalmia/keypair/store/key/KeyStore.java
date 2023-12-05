package com.github.cao.awa.kalmia.keypair.store.key;

import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;

import java.security.Key;

public abstract class KeyStore<T extends Key> {
    public T decode() {
        return decode(false);
    }

    public abstract T decode(boolean requireFailure);

    public abstract KeyPairStore keypairStone();

    public abstract byte[] key();
}
