package com.github.cao.awa.kalmia.network.crypto.symmetric.aes;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.network.crypto.symmetric.SymmetricCrypto;

public class AesCrypto extends SymmetricCrypto {
    @Override
    public byte[] encode(byte[] plains, byte[] cipher) throws Exception {
        return Crypto.aesEncrypt(plains, cipher);
    }

    @Override
    public byte[] decode(byte[] ciphertext, byte[] cipher) throws Exception {
        return  Crypto.aesDecrypt(ciphertext, cipher);
    }
}
