package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.client.polling.PollingClient;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage;
import com.github.cao.awa.kalmia.message.factor.MessageFactor;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.kalmia.session.factor.SessionFactor;
import com.github.cao.awa.kalmia.session.types.communal.CommunalSession;
import com.github.cao.awa.kalmia.session.types.duet.DuetSession;
import com.github.cao.awa.kalmia.session.types.group.GroupSession;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.UselessUser;
import com.github.cao.awa.kalmia.user.factor.UserFactor;
import com.github.cao.awa.kalmia.user.key.ServerKeyPairStoreFactor;
import com.github.cao.awa.kalmia.user.key.ec.EcServerKeyPair;
import com.github.cao.awa.kalmia.user.key.rsa.RsaServerKeyPair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kalmia {
    private static final Logger LOGGER = LogManager.getLogger("Kalmia");
    public static KalmiaServer SERVER;
    public static KalmiaClient CLIENT;

    public static void main(String[] args) {
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void startServer() throws Exception {
        setupEnvironment();

        LOGGER.info("Starting kalmia server");

        SERVER = new KalmiaServer(KalmiaServer.serverBootstrapConfig);

        LOGGER.info("Setup kalmia server");

        KalmiaEnv.setupServer();

        if (KalmiaServer.serverBootstrapConfig.translation()
                                              .enable()) {
            KalmiaTranslationEnv.setupFrameworks();
        }

        SERVER.startup();
    }

    public static void startClient() throws Exception {
        setupEnvironment();

        LOGGER.info("Starting kalmia client");

        CLIENT = new KalmiaClient(KalmiaClient.clientBootstrapConfig);

        LOGGER.info("Setup kalmia client");

        KalmiaEnv.setupClient();

        CLIENT.activeCallback(router -> {
            router.send(new ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
                                              "KalmiaWww v1.0.1"
            ));
        });

        CLIENT.connect();

        PollingClient.CLIENT = new PollingClient(CLIENT);
    }

    public static void setupEnvironment() {
        try {
            KalmiaServer.setupBootstrapConfig();
        } catch (Exception e) {

        }
        try {
            KalmiaClient.setupBootstrapConfig();
        } catch (Exception e) {

        }

        UserFactor.register(- 1,
                            UselessUser :: create
        );
        UserFactor.register(0,
                            DefaultUser :: create
        );

        MessageFactor.register(1,
                               PlainsMessage :: create
        );
        MessageFactor.register(- 1,
                               DeletedMessage :: create
        );

        ServerKeyPairStoreFactor.register(0,
                                          RsaServerKeyPair :: new
        );
        ServerKeyPairStoreFactor.register(1,
                                          EcServerKeyPair :: new
        );

        SessionFactor.register(1,
                               DuetSession :: create
        );
        SessionFactor.register(2,
                               CommunalSession :: create
        );
        SessionFactor.register(3,
                               GroupSession :: create
        );
    }
}
