package com.github.cao.awa.kalmia.security;

import java.security.interfaces.RSAPublicKey;

public class PreSharedRsaCipher extends PreSharedCipher<RSAPublicKey> {
    private final RSAPublicKey cipher;
    private final String key;

    public PreSharedRsaCipher(RSAPublicKey cipher, String key) {
        this.cipher = cipher;
        this.key = key;
    }

    @Override
    public RSAPublicKey cipher() {
        return this.cipher;
    }

    @Override
    public String key() {
        return this.key;
    }
}
