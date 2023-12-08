package com.github.cao.awa.kalmia.keypair.store.key.rsa;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.NotEncryptedException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

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
        return EntrustEnvironment.trys(() -> Crypto.decodeRsaPrikey(key()));
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
