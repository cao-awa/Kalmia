package com.github.cao.awa.kalmia.keypair.store.key.rsa;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.UnableToDecodeException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.security.interfaces.RSAPublicKey;

public class RsaPublicKeyStore extends KeyStore<RSAPublicKey> {
    private final KeyPairStore store;
    private final byte[] prikey;

    public RsaPublicKeyStore(KeyPairStore store, byte[] prikey) {
        this.store = store;
        this.prikey = prikey;
    }

    @Override
    public RSAPublicKey decode(boolean requireFailure) {
        if (requireFailure) {
            throw new UnableToDecodeException("The public unable to require failure");
        }
        return EntrustEnvironment.trys(() -> Crypto.decodeRsaPubkey(key()));
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
