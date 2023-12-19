package com.github.cao.awa.kalmia.keypair.pair.ec;

import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.kalmia.keypair.store.key.ec.EcPrivateKeyStore;
import com.github.cao.awa.kalmia.keypair.store.key.ec.EcPublicKeyStore;

import java.security.PrivateKey;
import java.security.PublicKey;

public class EcKeyPair extends KeyPairStore {
    public EcKeyPair(PureExtraIdentity identity, byte[] publicKey, byte[] privateKey) {
        super(identity,
              publicKey,
              privateKey
        );
    }

    @Override
    public int type() {
        return KeyStoreIdentity.EC_IDENTITY;
    }

    @Override
    public KeyStore<? extends PublicKey> createPublicStore(byte[] publicKey) {
        return new EcPublicKeyStore(this,
                                    publicKey
        );
    }

    @Override
    public KeyStore<? extends PrivateKey> createPrivateStore(byte[] privateKey) {
        return new EcPrivateKeyStore(this,
                                     privateKey
        );
    }
}
