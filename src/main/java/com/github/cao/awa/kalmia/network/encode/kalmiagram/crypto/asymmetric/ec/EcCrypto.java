package com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.asymmetric.ec;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.network.encode.kalmiagram.crypto.asymmetric.AsymmetricCrypto;
import org.jetbrains.annotations.Nullable;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class EcCrypto extends AsymmetricCrypto {
    public EcCrypto(@Nullable PublicKey pubkey, @Nullable PrivateKey prikey) {
        super(pubkey,
              prikey
        );
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        if (pubkey() == null) {
            return plains;
        }
        return Crypto.ecEncrypt(plains,
                                (ECPublicKey) pubkey()
        );
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        if (prikey() == null) {
            return ciphertext;
        }
        return Crypto.ecDecrypt(ciphertext,
                                (ECPrivateKey) prikey()
        );
    }
}
