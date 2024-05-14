package com.github.cao.awa.kalmia.keypair.store.key.rsa;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.NotEncryptedException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;

import java.security.interfaces.RSAPrivateKey;

public class RsaPrivateKeyStore extends KeyStore<RSAPrivateKey> {
    private final KeyPairStore store;
    private final byte[] prikey;

    public RsaPrivateKeyStore(KeyPairStore store, byte[] prikey) {
        this.store = store;
        this.prikey = prikey;
    }

    @Override
    public RSAPrivateKey decode(boolean requireFailure) {
        if (requireFailure) {
            try {
                Crypto.decodeRsaPrikey(key());

                throw new NotEncryptedException("The private key required to encrypt");
            } catch (Exception e) {
                return null;
            }
        }
        return Manipulate.supply(() -> Crypto.decodeRsaPrikey(key()))
                         .get();
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
