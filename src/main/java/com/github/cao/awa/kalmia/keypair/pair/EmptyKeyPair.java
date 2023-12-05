package com.github.cao.awa.kalmia.keypair.pair;

import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.keypair.store.key.KeyStore;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public class EmptyKeyPair extends KeyPairStore {
    public EmptyKeyPair() {
        super(BytesUtil.EMPTY,
              BytesUtil.EMPTY
        );
    }

    @Override
    public int type() {
        return KeyStoreIdentity.EMPTY_IDENTITY;
    }

    @Override
    public KeyStore<? extends PublicKey> createPublicStore(byte[] publicKey) {
        return null;
    }

    @Override
    public KeyStore<? extends PrivateKey> createPrivateStore(byte[] privateKey) {
        return null;
    }
}
