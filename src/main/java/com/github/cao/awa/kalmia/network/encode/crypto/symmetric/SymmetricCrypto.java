package com.github.cao.awa.kalmia.network.encode.crypto.symmetric;

import java.util.Arrays;

public abstract class SymmetricCrypto {
    private final byte[] cipher;

    protected SymmetricCrypto(byte[] cipher) {
        this.cipher = cipher;
    }

    public byte[] cipher() {
        return this.cipher;
    }

    public boolean isCipherEquals(byte[] cipher) {
        return Arrays.equals(cipher(),
                             cipher
        );
    }

    public abstract byte[] encode(byte[] plains) throws Exception;

    public abstract byte[] decode(byte[] ciphertext) throws Exception;
}
