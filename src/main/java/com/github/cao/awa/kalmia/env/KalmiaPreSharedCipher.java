package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.kalmia.security.cipher.manager.ec.EcPrikeyManager;
import com.github.cao.awa.kalmia.security.cipher.manager.ec.EcPubkeyManager;

public class KalmiaPreSharedCipher {
    public static String defaultCipherField = "Kalmia/Main";
    public static String expectCipherField = defaultCipherField;
    public static final EcPubkeyManager pubkeyManager = new EcPubkeyManager();
    public static final EcPrikeyManager prikeyManager = new EcPrikeyManager();
}
