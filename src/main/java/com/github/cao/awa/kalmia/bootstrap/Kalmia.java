package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kalmia {
    private static final Logger LOGGER = LogManager.getLogger("Kalmia");
    public static KalmiaServer SERVER;

    public static void main(String[] args) {
        try {
            KalmiaEnv.setupServer();

            SERVER = new KalmiaServer();

            SERVER.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
