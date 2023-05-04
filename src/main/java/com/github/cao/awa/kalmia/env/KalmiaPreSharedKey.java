package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.kalmia.security.cipher.manager.ec.EcPrikeyManager;
import com.github.cao.awa.kalmia.security.cipher.manager.ec.EcPubkeyManager;

public class KalmiaPreSharedKey {
    public static String defaultCipherKey = "Kalmia/Main";
    public static String expectCipherKey = defaultCipherKey;
    public static final EcPubkeyManager pubkeyManager = new EcPubkeyManager();
    public static final EcPrikeyManager prikeyManager = new EcPrikeyManager();
}
