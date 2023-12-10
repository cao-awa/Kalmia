package com.github.cao.awa.kalmia.keypair.store.key.ec;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.NotEncryptedException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.security.interfaces.ECPrivateKey;

public class EcPrivateKeyStore extends KeyStore<ECPrivateKey> {
    private final KeyPairStore store;
    private final byte[] prikey;

    public EcPrivateKeyStore(KeyPairStore store, byte[] prikey) {
        this.store = store;
        this.prikey = prikey;
    }

    @Override
    public ECPrivateKey decode(boolean requireFailure) {
        if (requireFailure) {
            try {
                Crypto.decodeEcPrikey(key());

                throw new NotEncryptedException("The private key required to encrypt");
            } catch (Exception e) {
                return null;
            }
        }
        return EntrustEnvironment.trys(() -> Crypto.decodeEcPrikey(key()));
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
