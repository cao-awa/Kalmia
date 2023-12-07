package com.github.cao.awa.kalmia.env;

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
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

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

    public static boolean setup = false;
    public static boolean serverSideLoading = true;
    public static final PacketFramework packetFramework = new PacketFramework();
    public static final BytesSerializeFramework BYTES_SERIALIZE_FRAMEWORK = new BytesSerializeFramework();
    public static final JsonSerializeFramework jsonSerializeFramework = new JsonSerializeFramework();
    public static final PluginFramework pluginFramework = new PluginFramework();
    public static final EventFramework eventFramework = new EventFramework();
    public static final NetworkEventFramework networkEventFramework = new NetworkEventFramework();
    public static final AwaitManager awaitManager = new AwaitManager();

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
