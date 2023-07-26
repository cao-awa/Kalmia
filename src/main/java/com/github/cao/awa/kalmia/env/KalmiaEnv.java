package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.resource.loader.ResourcesLoader;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.env.security.exception.PreShareKeyNotFoundException;
import com.github.cao.awa.kalmia.framework.event.EventFramework;
import com.github.cao.awa.kalmia.framework.network.event.NetworkEventFramework;
import com.github.cao.awa.kalmia.framework.network.unsolve.PacketSerializeFramework;
import com.github.cao.awa.kalmia.framework.plugin.PluginFramework;
import com.github.cao.awa.kalmia.framework.serialize.ByteSerializeFramework;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPublicKey;
import java.util.function.Supplier;

public class KalmiaEnv {
    public static boolean setup = false;
    public static boolean isServer = true;
    public static final PacketSerializeFramework packetSerializeFramework = new PacketSerializeFramework();
    public static final ByteSerializeFramework serializeFramework = new ByteSerializeFramework();
    public static final PluginFramework pluginFramework = new PluginFramework();
    public static final EventFramework eventFramework = new EventFramework();
    public static final NetworkEventFramework networkEventFramework = new NetworkEventFramework();

    public static void setupClient() throws PreShareKeyNotFoundException {
        isServer = false;

        setupPreSharedKey();
        setupFrameworks();

        setup = true;
    }

    public static void setupServer() throws PreShareKeyNotFoundException {
        isServer = true;

        setupPreSharedKey();
        setupFrameworks();

        setup = true;
    }

    public static void setupPreSharedKey() throws PreShareKeyNotFoundException {
        // Server prikey init.
        if (isServer) {
            KalmiaPreSharedKey.prikeyManager.add(KalmiaPreSharedKey.defaultCipherKey,
                                                 nullThenThrow(EntrustEnvironment.trys(() -> {
                                                                   return Crypto.decodeEcPrikey(Mathematics.toBytes(IOUtil.read(new InputStreamReader(ResourcesLoader.get("secret/kalmiagram/main/SECRET_PRIVATE"),
                                                                                                                                                      StandardCharsets.UTF_8
                                                                                                                    )),
                                                                                                                    36
                                                                   ));
                                                               }),
                                                               PreShareKeyNotFoundException :: new
                                                 )
            );
        }

        // Client pubkey init.
        ECPublicKey kalmiaMainPubkey = nullThenThrow(EntrustEnvironment.trys(() -> {
                                                         return Crypto.decodeEcPubkey(Mathematics.toBytes(IOUtil.read(new InputStreamReader(ResourcesLoader.get("secret/kalmiagram/main/SECRET_PUBLIC"),
                                                                                                                                            StandardCharsets.UTF_8
                                                                                                          )),
                                                                                                          36
                                                         ));
                                                     }),
                                                     PreShareKeyNotFoundException :: new
        );

        KalmiaPreSharedKey.pubkeyManager.add(KalmiaPreSharedKey.defaultCipherKey,
                                             kalmiaMainPubkey
        );
    }

    public static <E extends Exception, T> T nullThenThrow(T obj, Supplier<E> exception) throws E {
        if (obj == null) {
            throw exception.get();
        }
        return obj;
    }

    public static void setupFrameworks() {
        packetSerializeFramework.work();
        serializeFramework.work();
        pluginFramework.work();
        eventFramework.work();
        networkEventFramework.work();
    }
}
