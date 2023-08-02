package com.github.cao.awa.kalmia.user.key.ec;

import com.github.cao.awa.kalmia.user.key.ServerKeyPairStore;
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity;

public class EcServerKeyPair extends ServerKeyPairStore {
    public EcServerKeyPair(byte[] publicKey, byte[] privateKey) {
        super(publicKey,
              privateKey
        );
    }

    @Override
    public int type() {
        return PublicKeyIdentity.EC_IDENTITY;
    }
}
