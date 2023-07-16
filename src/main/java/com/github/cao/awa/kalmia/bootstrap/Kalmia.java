package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.DeletedUser;
import com.github.cao.awa.kalmia.user.DisabledUser;
import com.github.cao.awa.kalmia.user.factor.UserFactor;
import com.github.cao.awa.kalmia.user.key.ServerKeyPairStoreFactor;
import com.github.cao.awa.kalmia.user.key.ec.EcServerKeyPair;
import com.github.cao.awa.kalmia.user.key.rsa.RsaServerKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kalmia {
    private static final Logger LOGGER = LogManager.getLogger("Kalmia");
    public static KalmiaServer SERVER;

    public static void main(String[] args) throws Exception {
        setupEnvironment();

        startServer();
    }

    public static void startServer() throws Exception {
        LOGGER.info("Starting kalmia server");

        KalmiaEnv.setupServer();

        SERVER = new KalmiaServer();

        SERVER.startup();
    }

    public static void setupEnvironment() {
        UserFactor.register(- 1,
                            DeletedUser :: create
        );
        UserFactor.register(0,
                            DefaultUser :: create
        );
        UserFactor.register(1,
                            DisabledUser :: create
        );

        ServerKeyPairStoreFactor.register(0,
                                          RsaServerKeyPair :: new
        );
        ServerKeyPairStoreFactor.register(1,
                                          EcServerKeyPair :: new
        );
    }
}
