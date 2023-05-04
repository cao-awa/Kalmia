package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.kalmia.security.cipher.manager.rsa.RsaPrikeyManager;
import com.github.cao.awa.kalmia.security.cipher.manager.rsa.RsaPubkeyManager;

public class KalmiaPreSharedKey {
    public static String defaultCipherKey = "Kalmia/Main";
    public static String expectCipherKey = defaultCipherKey;
    public static final RsaPubkeyManager pubkeyManager = new RsaPubkeyManager();
    public static final RsaPrikeyManager prikeyManager = new RsaPrikeyManager();
}
