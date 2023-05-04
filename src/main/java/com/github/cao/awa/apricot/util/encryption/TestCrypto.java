package com.github.cao.awa.apricot.util.encryption;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

public class TestCrypto {
    /**
     * 生成密钥对(公钥和私钥)
     *
     * @return
     * @throws Exception
     */
    public static KeyPair initKey(int keySize) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("EC",
                                                                   "BC"
        );
        keyPairGen.initialize(keySize,
                              Crypto.RANDOM
        );
        return keyPairGen.generateKeyPair();
    }

    /**
     * 公钥加密
     *
     * @param data      源数据
     * @param publicKey 公钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, ECPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES",
                                           "BC"
        );
        cipher.init(Cipher.ENCRYPT_MODE,
                    publicKey
        );
        return cipher.doFinal(data);

    }

    /**
     * 私钥解密
     *
     * @param encryptedData 已加密数据
     * @param privateKey    私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedData, ECPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("ECIES",
                                           "BC"
        );
        cipher.init(Cipher.DECRYPT_MODE,
                    privateKey
        );
        return cipher.doFinal(encryptedData);
    }

    public static void main(String[] args) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            //初始化获取公钥和私钥
            KeyPair keypair = initKey(384);

            PublicKey publicKey = keypair.getPublic();
            PrivateKey privateKey = keypair.getPrivate();

            System.out.println("私钥原：" + privateKey);
            System.out.println("公钥原：" + publicKey);

            String publicKeyBase64 = Base64.getEncoder()
                                           .encodeToString(publicKey.getEncoded());
            String privateKeyBase64 = Base64.getEncoder()
                                            .encodeToString(privateKey.getEncoded());

            //生成固定公钥私钥
//            String publicKeyBase64 = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEvVlOVXJQe6yyLlCSCWQr246yay4Hl9qfB3C5S9al9t6cNzP3lwjJIRGzFmGywspn0OwiMJWmFV7daLhzCx79kQ==";
//            String privateKeyBase64 = "MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCDyvdnfevbZyiWDWOmwRp5hLDftlNWHzdD5YkiQW6hR6g==";

            String con = "Test, www";
            System.out.println("b: " + con);
            //加密
            byte[] content = encryptByPublicKey(con.getBytes(),
                                                (ECPublicKey) publicKey
            );
            //解密
            String contentDe = new String(decryptByPrivateKey(content,
                                                              (ECPrivateKey) privateKey
            ));
            //解密之后
            String deStr = contentDe;
            System.out.println("e: " + deStr);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
