package com.github.cao.awa.kalmia.keypair.pair.rsa;

import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.kalmia.keypair.store.key.pri.rsa.RsaPrivateKeyStore;
import com.github.cao.awa.kalmia.keypair.store.key.pub.rsa.RsaPublicKeyStore;

import java.security.PrivateKey;
import java.security.PublicKey;

public class RsaKeyPair extends KeyPairStore {
    public RsaKeyPair(byte[] publicKey, byte[] privateKey) {
        super(publicKey,
              privateKey
        );
    }

    @Override
    public int type() {
        return KeyStoreIdentity.RSA_IDENTITY;
    }


    @Override
    public KeyStore<? extends PublicKey> createPublicStore(byte[] publicKey) {
        return new RsaPublicKeyStore(this,
                                     publicKey
        );
    }

    @Override
    public KeyStore<? extends PrivateKey> createPrivateStore(byte[] privateKey) {
        return new RsaPrivateKeyStore(this,
                                      privateKey
        );
    }
}
