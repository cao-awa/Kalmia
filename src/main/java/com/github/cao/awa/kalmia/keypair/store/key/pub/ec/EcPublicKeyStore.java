package com.github.cao.awa.kalmia.keypair.store.key.pub.ec;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.UnableToDecodeException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;

import java.security.interfaces.ECPublicKey;

public class EcPublicKeyStore extends KeyStore<ECPublicKey> {
    private final KeyPairStore store;
    private final byte[] prikey;

    public EcPublicKeyStore(KeyPairStore store, byte[] prikey) {
        this.store = store;
        this.prikey = prikey;
    }

    @Override
    public ECPublicKey decode(boolean requireFailure) {
        if (requireFailure) {
            throw new UnableToDecodeException("The public unable to require failure");
        }
        return Crypto.decodeEcPubkey(key());
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
