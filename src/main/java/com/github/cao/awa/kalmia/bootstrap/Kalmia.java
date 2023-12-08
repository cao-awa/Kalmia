package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.client.polling.PollingClient;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.keypair.pair.ec.EcKeyPair;
import com.github.cao.awa.kalmia.keypair.pair.empty.EmptyKeyPair;
import com.github.cao.awa.kalmia.keypair.pair.rsa.RsaKeyPair;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStoreFactor;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.crypt.AsymmetricCryptedMessage;
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

        setupTest();

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

        PollingClient.CLIENT = new PollingClient(CLIENT);

        CLIENT.connect();
    }

    public static void setupTest() throws Exception {
        SERVER.keypairManager()
              .set(0,
                   new EcKeyPair(
                           Mathematics.toBytes(
                                   "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vno5s6a3805phsxo2h3alhfv4j82g4rmnma75r2z455u3q3vwa6bnnhhuasjqn7t6ej56cg6kv4x21osmu14m09om1ofhzb1xqf6d69z8dnophc8o34h0az66852dfa8yth8oxz2t6yf3c0lhjhxbpml2dh0y5yg7ueendizsw3dslncr2ur6edjix8fnaifeygo6dhdcj5hd6",
                                   36
                           ),
                           Crypto.aesEncrypt(
                                   Mathematics.toBytes(
                                           "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq360weg0c0t5vwpgs8tbpxngxghfsebzbnnyjs84lq09jlz549yznbms2axywvzbbl4ah7vzkgxknv0f2rmlvjw0i8hhzkkbhflbk3z24scim36jx3exfl87qh1m876ckxf4ixw0vcgacr9zjdhnh35gyv0mhme6k77ee15uz504gbioal8oq493vu6ucfg95nfir7kzfze3oczf2gn00ur9t8gaalmcdcrfw2b9i336sqltk996voz4nj4pfn2muutcmctl9j2uk3kjzsi40w9xo2mk6d5siu9n0czmvxoyc0f7zbtau9qtf9k8t5p57fyaxy74eiofe",
                                           36
                                   ),
                                   KalmiaEnv.testUer1AesCipher
                           )
                   )
              );

        SERVER.keypairManager()
              .set(1,
                   new EcKeyPair(
                           Mathematics.toBytes(
                                   "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vnnwq19j63xhbkp6z60qmcdj2z28zwce465oiv9vpwkhkqdyeibxus1xui5tdljkef2xcb121bb68c6vcsm8g3jlipz5ok13ygifen7s13lhyb9nm11nr6qg80oifk7ahlrj0ta0jek0lcocgv4oaxwoutza2pvq3k927l232tkfveyfiz0sa7jod4ph2wqdqy50vu80spoqbj",
                                   36
                           ),
                           Crypto.aesEncrypt(
                                   Mathematics.toBytes(
                                           "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq2eypp3dstdehqpd4m5efztu0ybkzru0ze47024geehf8o9wr0u3tfqqjv6lrdbn2yphsfl3tox8i0wlswyjz0zmn5mhyj9x26zqa6ydrvwaxeyoh3963unm75jxdg3wqtpx6ckeoghwbigutg0cbicq6o3ndjdkf7fz1nx9igrdgmvyj2b00s02nyor7qh3k1ebvxmib30yksqjq4h65kqw8x4kpyamq8of75ppmssfg7u3xusg525i44undo2emciy30qbjj9lkw0at83x6nozotbdgnfvaccpn7v2atys9manaaa9baz5k6nwu90ekoxsg2q18hikf",
                                           36
                                   ),
                                   KalmiaEnv.testUer2AesCipher
                           )
                   )
              );
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

        MessageFactor.register(- 1,
                               DeletedMessage :: create
        );
        MessageFactor.register(1,
                               PlainsMessage :: create
        );
        MessageFactor.register(2,
                               AsymmetricCryptedMessage :: create
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
