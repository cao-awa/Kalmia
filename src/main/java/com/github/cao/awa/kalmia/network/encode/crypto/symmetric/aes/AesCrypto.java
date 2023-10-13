package com.github.cao.awa.kalmia.network.encode.crypto.symmetric.aes;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class AesCrypto extends SymmetricCrypto {
    public AesCrypto(byte[] cipher) {
        super(cipher);
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        return BytesUtil.concat(Crypto.aesEncrypt(plains,
                                                  cipher(),
                                                  iv()
                                )
        );
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        return Crypto.aesDecrypt(ciphertext,
                                 cipher(),
                                 iv()
        );
    }
}
