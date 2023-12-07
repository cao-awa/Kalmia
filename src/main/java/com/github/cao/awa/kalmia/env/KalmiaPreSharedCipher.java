package com.github.cao.awa.kalmia.env;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.resource.loader.ResourceLoader;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.security.key.PreSharedCipherEncode;
import com.github.cao.awa.kalmia.security.cipher.manager.ec.EcPrikeyManager;
import com.github.cao.awa.kalmia.security.cipher.manager.ec.EcPubkeyManager;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

public class KalmiaPreSharedCipher {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaPreSharedCipher");
    public static String defaultCipherField = "Kalmia/Main";
    public static String expectCipherField = defaultCipherField;
    public static final EcPubkeyManager pubkeyManager = new EcPubkeyManager();
    public static final EcPrikeyManager prikeyManager = new EcPrikeyManager();

    public static void setupCiphers() {
        LOGGER.info("Preparing ciphers");

        setupMain();

        File ciphersDir = new File(KalmiaConstant.KEYPAIR_STORAGE_PATH);
        File[] cipherDirs = ciphersDir.listFiles();

        if (cipherDirs != null) {
            for (File cipherDir : cipherDirs) {
                setupCipher(cipherDir.getAbsolutePath());
            }
        }
    }

    public static void setupCipher(String cipherDir) {
        File metaFile = new File(cipherDir + "/cipher.json");
        File secretPublicFile = new File(cipherDir + "/SECRET_PUBLIC");
        File secretPrivateFile = new File(cipherDir + "/SECRET_PRIVATE");

        if (! metaFile.isFile()) {
            LOGGER.warn("The cipher in '{}' is missing metadata file, will not be load",
                        metaFile.getAbsolutePath()
            );
        }

        JSONObject metadata = EntrustEnvironment.trys(() -> JSONObject.parse(IOUtil.read(new FileReader(metaFile))));

        assert metadata != null;
        String cipherName = metadata.getString("cipher-name");

        setupPublicKey(secretPublicFile,
                       cipherName
        );

        setupPrivateKey(secretPrivateFile,
                        cipherName
        );
    }

    public static void setupPublicKey(File file, String fieldName) {
        if (file.isFile()) {
            JSONObject secretPublic = EntrustEnvironment.trys(() -> JSONObject.parse(IOUtil.read(new FileReader(file))));

            if (secretPublic != null) {
                ECPublicKey publicKey = PreSharedCipherEncode.decodeEcPublic(secretPublic);

                pubkeyManager.add(fieldName,
                                  publicKey
                );
            }
        }
    }

    public static void setupPrivateKey(File file, String fieldName) {
        if (file.isFile()) {
            JSONObject secretPrivate = EntrustEnvironment.trys(() -> JSONObject.parse(IOUtil.read(new FileReader(file))));

            if (secretPrivate != null) {
                ECPrivateKey publicKey = PreSharedCipherEncode.decodeEcPrivate(secretPrivate);

                prikeyManager.add(fieldName,
                                  publicKey
                );
            }
        }
    }

    private static void setupMain() {
        new File("ciphers/main").mkdirs();

        EntrustEnvironment.trys(() -> {
                                    IOUtil.write(new FileOutputStream(KalmiaConstant.MAIN_KEYPAIR_META_PATH),
                                                 ResourceLoader.get("kalmiagram/secret/main/cipher.json")
                                    );
                                }
        );

        EntrustEnvironment.trys(() -> {
                                    InputStream input = ResourceLoader.get("kalmiagram/secret/main/SECRET_PRIVATE");

                                    if (input == null) {
                                        return;
                                    }

                                    IOUtil.write(new FileOutputStream(KalmiaConstant.MAIN_PRIVATE_KEY_PATH),
                                                 input
                                    );
                                }
        );

        EntrustEnvironment.trys(() -> {
                                    IOUtil.write(new FileOutputStream(KalmiaConstant.MAIN_PUBLIC_KEY_PATH),
                                                 ResourceLoader.get("kalmiagram/secret/main/SECRET_PUBLIC")
                                    );
                                }
        );
    }
}
