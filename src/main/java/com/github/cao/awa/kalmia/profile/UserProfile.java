package com.github.cao.awa.kalmia.profile;

import com.github.cao.awa.apricot.util.encryption.Crypto;

import java.security.interfaces.ECPublicKey;

public class UserProfile {
    private final ECPublicKey publicKey;

    public UserProfile(byte[] publicKey) {
        this.publicKey = Crypto.decodeEcPubkey(publicKey);
    }
}
