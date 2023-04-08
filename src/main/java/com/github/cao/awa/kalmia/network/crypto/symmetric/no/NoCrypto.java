package com.github.cao.awa.kalmia.network.crypto.symmetric.no;

import com.github.cao.awa.kalmia.network.crypto.symmetric.SymmetricCrypto;

public class NoCrypto extends SymmetricCrypto {
    @Override
    public byte[] encode(byte[] plains, byte[] cipher) throws Exception {
        return plains;
    }

    @Override
    public byte[] decode(byte[] ciphertext, byte[] cipher) throws Exception {
        return ciphertext;
    }
}
