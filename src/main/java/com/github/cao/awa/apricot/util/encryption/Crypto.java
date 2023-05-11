package com.github.cao.awa.apricot.util.encryption;

import com.github.cao.awa.apricot.anntation.Stable;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Stable
public class Crypto {
    private static final Logger DEBUG = LogManager.getLogger("Debugger");
    private static final byte[] DEFAULT_KEY_IV = new byte[]{102, - 123, 114, - 106, - 40, - 35, 71, - 2, - 89, 81, 13, 47, - 79, 89, 121, - 23};
    public static final String RSA_ALGORITHM = "RSA";
    public static final String BC_PROVIDER = "BC";
    public static final String EC_ALGORITHM = "EC";
    public static final SecureRandom RANDOM = new SecureRandom();

    static {
        Security.setProperty("crypto.policy",
                             "unlimited"
        );
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] defaultIv() {
        return DEFAULT_KEY_IV.clone();
    }

    public static byte[] aesDecrypt(byte[] content, byte[] cipher) throws Exception {
        return aesDecrypt(content,
                          cipher,
                          DEFAULT_KEY_IV
        );
    }

    public static byte[] aesEncrypt(byte[] content, byte[] cipher) throws Exception {
        return aesEncrypt(content,
                          cipher,
                          DEFAULT_KEY_IV
        );
    }

    public static byte[] aesDecrypt(byte[] content, byte[] cipher, byte[] iv) throws Exception {
        if (iv.length != 16) {
            iv = DEFAULT_KEY_IV;
        }
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(Cipher.DECRYPT_MODE,
                      new SecretKeySpec(cipher,
                                        "AES"
                      ),
                      new IvParameterSpec(iv)
        );
        return instance.doFinal(content);
    }

    public static byte[] aesEncrypt(byte[] content, byte[] cipher, byte[] iv) throws Exception {
        if (iv.length != 16) {
            iv = DEFAULT_KEY_IV;
        }
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(Cipher.ENCRYPT_MODE,
                      new SecretKeySpec(cipher,
                                        "AES"
                      ),
                      new IvParameterSpec(iv)
        );
        return instance.doFinal(content);
    }

    public static KeyPair rsaKeypair(int size) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM,
                                                                         BC_PROVIDER
        );
        keyPairGenerator.initialize(size,
                                    RANDOM
        );
        return keyPairGenerator.genKeyPair();
    }

    public static byte[] rsaEncrypt(byte[] content, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM,
                                           BC_PROVIDER
        );
        cipher.init(Cipher.ENCRYPT_MODE,
                    publicKey
        );
        return cipher.doFinal(content);
    }

    public static byte[] rsaDecrypt(byte[] content, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM,
                                           BC_PROVIDER
        );
        cipher.init(Cipher.DECRYPT_MODE,
                    privateKey
        );
        return cipher.doFinal(content);
    }

    public static byte[] encodeRsaPubkey(RSAPublicKey key) {
        return key.getEncoded();
    }

    public static RSAPublicKey decodeRsaPubkey(byte[] key) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return EntrustEnvironment.cast(keyFactory.generatePublic(keySpec));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static RSAPrivateKey decodeRsaPrikey(byte[] key) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return EntrustEnvironment.cast(keyFactory.generatePrivate(keySpec));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static KeyPair ecKeyPair(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(EC_ALGORITHM,
                                                                   BC_PROVIDER
        );
        keyPairGen.initialize(keySize,
                              RANDOM
        );
        return keyPairGen.generateKeyPair();
    }

    public static byte[] ecEncrypt(byte[] data, ECPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES",
                                           "BC"
        );
        cipher.init(Cipher.ENCRYPT_MODE,
                    publicKey
        );
        return cipher.doFinal(data);

    }

    public static byte[] ecDecrypt(byte[] encryptedData, ECPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES",
                                           "BC"
        );
        cipher.init(Cipher.DECRYPT_MODE,
                    privateKey
        );
        return cipher.doFinal(encryptedData);
    }

    public static ECPublicKey decodeEcPubkey(byte[] key) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return EntrustEnvironment.cast(keyFactory.generatePublic(keySpec));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static ECPrivateKey decodeEcPrikey(byte[] key) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return EntrustEnvironment.cast(keyFactory.generatePrivate(keySpec));
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static byte[] ecSign(byte[] content, ECPrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA512WithECDSA");
        sign.initSign(privateKey);
        sign.update(content);
        return sign.sign();
    }


    public static Boolean ecVerify(byte[] contentSource, byte[] contentSign, ECPublicKey pubKey) throws Exception {
        Signature sign = Signature.getInstance("SHA512WithECDSA");
        sign.initVerify(pubKey);
        sign.update(contentSource);
        return sign.verify(contentSign);
    }

    public static byte[] rsaSign(byte[] content, RSAPrivateKey privateKey) throws Exception {
        Signature sign = Signature.getInstance("SHA512WithRSA");
        sign.initSign(privateKey);
        sign.update(content);
        return sign.sign();
    }


    public static Boolean rsaVerify(byte[] contentSource, byte[] contentSign, RSAPublicKey pubKey) throws Exception {
        Signature sign = Signature.getInstance("SHA512WithRSA");
        sign.initVerify(pubKey);
        sign.update(contentSource);
        return sign.verify(contentSign);
    }
}
