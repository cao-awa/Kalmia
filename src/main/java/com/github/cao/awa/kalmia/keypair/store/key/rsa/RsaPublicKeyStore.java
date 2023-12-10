package com.github.cao.awa.kalmia.keypair.store.key.rsa;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.UnableToDecodeException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;

import java.security.interfaces.RSAPublicKey;

public class RsaPublicKeyStore extends KeyStore<RSAPublicKey> {
    private final KeyPairStore store;
    private final byte[] pubkey;

    public RsaPublicKeyStore(KeyPairStore store, byte[] pubkey) {
        this.store = store;
        this.pubkey = pubkey;
    }

    @Override
    public RSAPublicKey decode(boolean requireFailure) {
        if (requireFailure) {
            throw new UnableToDecodeException("The public unable to require failure");
        }
        return Crypto.decodeRsaPubkey(key());
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
