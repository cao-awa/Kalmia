package com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.asymmetric.rsa;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.asymmetric.AsymmetricCrypto;
import org.jetbrains.annotations.Nullable;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RsaCrypto extends AsymmetricCrypto {
    public RsaCrypto(@Nullable PublicKey pubkey, @Nullable PrivateKey prikey) {
        super(pubkey,
              prikey
        );
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        if (pubkey() == null) {
            return plains;
        }
        return Crypto.rsaEncrypt(plains,
                                 (RSAPublicKey) pubkey()
        );
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        if (prikey() == null) {
            return ciphertext;
        }
        return Crypto.rsaDecrypt(ciphertext,
                                 (RSAPrivateKey) prikey()
        );
    }
}
