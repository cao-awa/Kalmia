package com.github.cao.awa.kalmia.user.key.rsa;

import com.github.cao.awa.kalmia.user.key.ServerKeyPairStore;
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity;

public class RsaServerKeyPair extends ServerKeyPairStore {
    public RsaServerKeyPair(byte[] publicKey, byte[] privateKey) {
        super(publicKey,
              privateKey
        );
    }

    @Override
    public int type() {
        return PublicKeyIdentity.RSA_IDENTITY;
    }
}
