package com.github.cao.awa.kalmia.network.encode.crypto.symmetric;

import com.github.cao.awa.kalmia.network.encode.crypto.LayerCrypto;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.Arrays;

public abstract class SymmetricCrypto extends LayerCrypto {
    private byte[] cipher;
    private byte[] iv = BytesUtil.EMPTY;

    public SymmetricCrypto(byte[] cipher) {
        this.cipher = cipher;
    }

    public SymmetricCrypto(byte[] cipher, byte[] iv) {
        this.cipher = cipher;
        this.iv = iv;
    }

    public void cipher(byte[] cipher) {
        this.cipher = cipher;
    }

    public byte[] cipher() {
        return this.cipher;
    }

    public void iv(byte[] cipher) {
        this.iv = cipher;
    }

    public byte[] iv() {
        return this.iv;
    }

    public boolean isCipherEquals(byte[] cipher) {
        return Arrays.equals(cipher(),
                             cipher
        );
    }
}
