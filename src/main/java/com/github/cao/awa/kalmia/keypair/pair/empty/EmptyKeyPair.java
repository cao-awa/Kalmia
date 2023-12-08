package com.github.cao.awa.kalmia.keypair.pair.empty;

import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.kalmia.keypair.store.key.empty.EmptyPrivateKeyStore;
import com.github.cao.awa.kalmia.keypair.store.key.empty.EmptyPublicKeyStore;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public class EmptyKeyPair extends KeyPairStore {
    public EmptyKeyPair() {
        super(BytesUtil.EMPTY,
              BytesUtil.EMPTY
        );
    }

    public EmptyKeyPair(byte[] publicKey, byte[] privateKey) {
        super(publicKey,
              privateKey
        );
    }

    @Override
    public int type() {
        return KeyStoreIdentity.EMPTY_IDENTITY;
    }

    @Override
    public KeyStore<? extends PublicKey> createPublicStore(byte[] publicKey) {
        return new EmptyPublicKeyStore(this,
                                       publicKey
        );
    }

    @Override
    public KeyStore<? extends PrivateKey> createPrivateStore(byte[] privateKey) {
        return new EmptyPrivateKeyStore(this,
                                        privateKey
        );
    }
}
