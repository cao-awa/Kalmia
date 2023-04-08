package com.github.cao.awa.apricot.util.encryption;

import com.github.cao.awa.apricot.anntations.Stable;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Stable
public class Crypto {
    private static final Logger DEBUG = LogManager.getLogger("Debugger");
    private static final byte[] KEY_VI = "0000000000000000".getBytes();

    static {
        Security.setProperty("crypto.policy",
                             "unlimited"
        );
    }

    public static byte[] aesDecrypt(byte[] content, byte[] cipher) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(Cipher.DECRYPT_MODE,
                      new SecretKeySpec(cipher,
                                        "AES"
                      ),
                      new IvParameterSpec(KEY_VI)
        );
        return instance.doFinal(content);
    }

    public static byte[] aesEncrypt(byte[] content, byte[] cipher) throws Exception {
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(Cipher.ENCRYPT_MODE,
                      new SecretKeySpec(cipher,
                                        "AES"
                      ),
                      new IvParameterSpec(KEY_VI)
        );
        return instance.doFinal(content);
    }

    public static void main(String[] args) {
        try {
            String message = "xxx";

            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(8192);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

            long start = TimeUtil.millions();

            byte[] e = rsaEncrypt(message.getBytes(),
                                  publicKey
            );

            DEBUG.info("Encrypt done in {}ms",
                       TimeUtil.processMillion(start)
            );
            DEBUG.info("Message：" + new String(e));

            start = TimeUtil.millions();

            byte[] de = rsaDecrypt(e,
                                   privateKey
            );

            DEBUG.info("Decrypt done in {}ms",
                       TimeUtil.processMillion(start)
            );
            DEBUG.info("Message：" + new String(de));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] rsaEncrypt(byte[] content, RSAPublicKey publicKey) throws Exception {
        Cipher instance = Cipher.getInstance("RSA");
        instance.init(Cipher.ENCRYPT_MODE,
                      publicKey
        );
        return instance.doFinal(content);
    }

    public static byte[] rsaDecrypt(byte[] content, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
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
            X509EncodedKeySpec encodedKeySpec = new X509EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return EntrustEnvironment.cast(keyFactory.generatePublic(encodedKeySpec));
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public static RSAPrivateKey decodeRsaPrikey(byte[] key) {
        try {
            PKCS8EncodedKeySpec encodedKeySpec = new PKCS8EncodedKeySpec(key);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return EntrustEnvironment.cast(keyFactory.generatePrivate(encodedKeySpec));
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
