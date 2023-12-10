package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.await.AwaitManager;
import com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.ClientBootstrapConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.network.ClientNetworkConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.meta.BootstrapConfigMeta;
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.ServerBootstrapConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.network.ServerNetworkConfig;
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.translation.BootstrapTranslationConfig;
import com.github.cao.awa.kalmia.framework.event.EventFramework;
import com.github.cao.awa.kalmia.framework.network.event.NetworkEventFramework;
import com.github.cao.awa.kalmia.framework.network.unsolve.PacketFramework;
import com.github.cao.awa.kalmia.framework.plugin.PluginFramework;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializeFramework;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializeFramework;
import com.github.cao.awa.kalmia.keypair.pair.ec.EcKeyPair;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.resource.manager.ResourcesManager;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.File;

public class KalmiaEnv {
    public static final String VERSION = "1.0.0";

    public static final ServerBootstrapConfig DEFAULT_SERVER_BOOTSTRAP_CONFIG = new ServerBootstrapConfig(
            new BootstrapConfigMeta(0),
            new ServerNetworkConfig(
                    "127.0.0.1",
                    12345,
                    true
            ),
            new BootstrapTranslationConfig(
                    false
            )
    );

    public static final ClientBootstrapConfig DEFAULT_CLIENT_BOOTSTRAP_CONFIG = new ClientBootstrapConfig(
            new BootstrapConfigMeta(0),
            new ClientNetworkConfig(
                    "127.0.0.1",
                    12345,
                    true
            )
    );

    public static final byte[] CHALLENGE_DATA = new byte[]{
            123, 123, 123, 123,
            121, 121, 121, 121,
            111, 111, 111, 111,
            101, 101, 101, 101,
    };

    public static final byte[] testUer1AesCipher = new byte[]{
            11, 11, 11, 11,
            12, 12, 12, 12,
            13, 13, 13, 13,
            14, 14, 14, 14,
    };

    public static final DefaultUser testUser1 = EntrustEnvironment.trys(() -> {
        return new DefaultUser(TimeUtil.millions(),
                               "123456"
        );
    });

    public static final byte[] testUer2AesCipher = new byte[]{
            10, 11, 12, 13,
            11, 12, 13, 14,
            12, 13, 14, 15,
            13, 14, 15, 16,
    };

    public static final DefaultUser testUser2 = EntrustEnvironment.trys(() -> {
        return new DefaultUser(TimeUtil.millions(),
                               "123456"
        );
    });

    public static final EcKeyPair testKeypair0 = EntrustEnvironment.trys(() -> new EcKeyPair(
            Mathematics.toBytes(
                    "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vnnw5452hwwjfm3hfg82429nt0wmbnwvwceupcz9bq3ys9jy0ti2mhuc2l3ulhpk2udvbbhlgq81wutwk4lgmr1mq8htrozgeqnj8t8sfmke4bap74halvb7vib9e5zo5y9uuvirbc7rsujhh84yy0r7h9qb20oz2unblv2h42ip17dimvzhuedr64l8a7o6evsy8wh9o2ut5g",
                    36
            ),
            Crypto.aesEncrypt(
                    Mathematics.toBytes(
                            "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq0uzjtnj011l4cjtpjvyquwy6q35zsfp0ysv8qec3d94hn1cnzlpjw42tk32hotwo8j9nkckaxq8m3mtsjlp4umzfpvir2anxqbywu7m8ffrphfu5a5dz6fu9ixf7ijr3h9hkg9iktw48gu3jgotd95bx48jch0n7g9ebdl0j1he6yzz2gxgpl68w6waob18m0g2oavhy0484ob71c44zxm2gf5fiyyyg94w420h4ljhmmy6qd8i7md0h7owgckdw80f3as4orbps74ls3x537i32try0af9ivw5ttnxy6jkv5e0r6ukwup0v7n82oy2ujkxpvh9q21as",
                            36
                    ),
                    KalmiaEnv.testUer1AesCipher
            )
    ));

    public static final EcKeyPair testKeypair1 = EntrustEnvironment.trys(() -> new EcKeyPair(
            Mathematics.toBytes(
                    "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vnobd6b4d2kz1lqhz5hs7j0ledngfd1s7zobjb31fkhpsyu8fje35hk5ott7y0qc14k0qqgo5fe8c828gxnhlfx21pxtmaug75xam51q7u2ps7oik6e7h6zc6waggzt9jx366jsg56c9vmux0mmwqdkhxydgdtm3ytlgmn6qlo8y11btd91rw8l76a3pycebz97y9zg6gbtxau",
                    36
            ),
            Crypto.aesEncrypt(
                    Mathematics.toBytes(
                            "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq0w3yyyd3w6vxrbmgut17dpv22sdjszi9fgo4xua39yzjhjkc6ctbx13vrxczljd9o99ca4003euw7xfcqzn3knyrgcoayd91nridjdi3kie1yscsllu3t4j54idq2aunifu92porvqya7ujrr27kinkyjgcoez52etng87t55orgpheb2rmaxlw3bdmckihtauoi9ti2ai6yc89rahqkpi6rw6mkrdswolwh6i8pu1s3h52s8s5tr4ctv1vn8fs91ml34afpxsxn4z2uninvra2unyn326itvnn1uqlhy1dsgsbgxa7wlqzaqn3ymn369s6zvhwftn12",
                            36
                    ),
                    KalmiaEnv.testUer2AesCipher
            )
    ));

    public static boolean setup = false;
    public static boolean serverSideLoading = true;
    public static final PacketFramework packetFramework = new PacketFramework();
    public static final BytesSerializeFramework BYTES_SERIALIZE_FRAMEWORK = new BytesSerializeFramework();
    public static final JsonSerializeFramework jsonSerializeFramework = new JsonSerializeFramework();
    public static final PluginFramework pluginFramework = new PluginFramework();
    public static final EventFramework eventFramework = new EventFramework();
    public static final NetworkEventFramework networkEventFramework = new NetworkEventFramework();
    public static final AwaitManager awaitManager = new AwaitManager();

    static {
        new File("data/resources").mkdirs();
    }

    public static final ResourcesManager resourceManager = new ResourcesManager("data/resources");

    public static void setupClient() {
        UnsolvedPacketFactor.register();

        serverSideLoading = false;

        setupPreSharedKey();
        setupFrameworks();

        setup = true;
    }

    public static void setupServer() {
        UnsolvedPacketFactor.register();

        serverSideLoading = true;

        setupPreSharedKey();
        setupFrameworks();

        setup = true;
    }

    public static void setupPreSharedKey() {
        KalmiaPreSharedCipher.setupCiphers();
    }

    public static void setupFrameworks() {
        packetFramework.work();
        BYTES_SERIALIZE_FRAMEWORK.work();
        jsonSerializeFramework.work();
        pluginFramework.work();
        eventFramework.work();
        networkEventFramework.work();
    }
}
