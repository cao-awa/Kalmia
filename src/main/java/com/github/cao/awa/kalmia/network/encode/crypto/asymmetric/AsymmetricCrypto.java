package com.github.cao.awa.kalmia.network.encode.crypto.asymmetric;

import com.github.cao.awa.kalmia.network.encode.crypto.TransportLayerCrypto;

import java.security.PrivateKey;
import java.security.PublicKey;

public abstract class AsymmetricCrypto extends TransportLayerCrypto {
    private PublicKey pubkey;
    private PrivateKey prikey;

    public AsymmetricCrypto(PublicKey pubkey, PrivateKey prikey) {
        this.pubkey = pubkey;
        this.prikey = prikey;
    }

    public PublicKey pubkey() {
        return this.pubkey;
    }

    public void pubkey(PublicKey pubkey) {
        this.pubkey = pubkey;
    }

    public PrivateKey prikey() {
        return this.prikey;
    }

    public void prikey(PrivateKey prikey) {
        this.prikey = prikey;
    }
}
