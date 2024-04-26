package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.client.polling.PollingClient;
import com.github.cao.awa.kalmia.config.KalmiaConfig;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.keypair.pair.ec.EcKeyPair;
import com.github.cao.awa.kalmia.keypair.pair.empty.EmptyKeyPair;
import com.github.cao.awa.kalmia.keypair.pair.rsa.RsaKeyPair;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStoreFactor;
import com.github.cao.awa.kalmia.message.cover.CoverMessage;
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage;
import com.github.cao.awa.kalmia.message.factor.MessageFactor;
import com.github.cao.awa.kalmia.message.user.UserMessage;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.kalmia.session.communal.CommunalSession;
import com.github.cao.awa.kalmia.session.duet.DuetSession;
import com.github.cao.awa.kalmia.session.factor.SessionFactor;
import com.github.cao.awa.kalmia.session.group.GroupSession;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.UselessUser;
import com.github.cao.awa.kalmia.user.factor.UserFactor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

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

        SERVER = new KalmiaServer();

        KalmiaEnv.setupServer();

        LOGGER.info("Setup kalmia server");

        if (SERVER.serverBootstrapConfig.get()
                .translation.get()
                .enabled.get()) {
            KalmiaTranslationEnv.setupFrameworks();
        }

        setupTest();

        SERVER.startup();
    }

    public static void startClient() throws Exception {
        setupEnvironment();

        LOGGER.info("Starting kalmia client");

        CLIENT = new KalmiaClient();

        LOGGER.info("Setup kalmia client");

        KalmiaEnv.setupClient();

        CLIENT.activeCallback(router -> {
            router.send(new ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
                                              "KalmiaWww v1.0.1"
            ));
        });

        PollingClient.CLIENT = new PollingClient(CLIENT);

        CLIENT.connect();
    }

    public static void setupTest() throws Exception {
        Set<PureExtraIdentity> keys = SERVER.userManager()
                                            .keyStores(KalmiaEnv.testUser1.identity());

        keys.add(KalmiaEnv.testKeypair0.identity());
        keys.add(KalmiaEnv.testKeypair1.identity());

        SERVER.userManager()
              .keyStores(KalmiaEnv.testUser1.identity(),
                         keys
              );

        SERVER.keypairManager()
              .set(KalmiaEnv.testKeypair0.identity(),
                   KalmiaEnv.testKeypair0
              );

        SERVER.keypairManager()
              .set(KalmiaEnv.testKeypair1.identity(),
                   KalmiaEnv.testKeypair1
              );
    }

    public static void setupEnvironment() {
        KalmiaEnv.setupConfigFramework();

        KalmiaConfig.createEntry(
                KalmiaEnv.globalConfig,
                ReflectionFramework.fetchField(
                        KalmiaEnv.class,
                        "globalConfig"
                )
        );

        System.out.println(KalmiaEnv.globalConfig.get().enabledTranslation.get());

        UserFactor.register(- 1,
                            UselessUser :: create
        );
        UserFactor.register(0,
                            DefaultUser :: create
        );

        MessageFactor.register(- 1,
                               DeletedMessage :: create
        );
        MessageFactor.register(2,
                               UserMessage :: create
        );
        MessageFactor.register(5,
                               CoverMessage :: create
        );

        KeyPairStoreFactor.register(0,
                                    RsaKeyPair :: new
        );
        KeyPairStoreFactor.register(1,
                                    EcKeyPair :: new
        );
        KeyPairStoreFactor.register(123,
                                    EmptyKeyPair :: new
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
