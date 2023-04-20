package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.KalmiaServer;
import com.github.cao.awa.kalmia.framework.network.unsolve.UnsolvedPacketFramework;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kalmia {
    private static final Logger LOGGER = LogManager.getLogger("Kalmia");
    public static final KalmiaServer SERVER = new KalmiaServer();
    public static final UnsolvedPacketFramework UNSOLVED_FRAMEWORK = new UnsolvedPacketFramework();

    public static void main(String[] args) {
        try {
            UNSOLVED_FRAMEWORK.work();

            SERVER.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
