package com.github.cao.awa.kalmia.keypair.store.key.ec;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.keypair.exception.UnableToDecodeException;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;

import java.security.interfaces.ECPublicKey;

public class EcPublicKeyStore extends KeyStore<ECPublicKey> {
    private final KeyPairStore store;
    private final byte[] pubkey;

    public EcPublicKeyStore(KeyPairStore store, byte[] pubkey) {
        this.store = store;
        this.pubkey = pubkey;
    }

    @Override
    public ECPublicKey decode(boolean requireFailure) {
        if (requireFailure) {
            throw new UnableToDecodeException("The public unable to require failure");
        }
        return Crypto.decodeEcPubkey(key());
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
