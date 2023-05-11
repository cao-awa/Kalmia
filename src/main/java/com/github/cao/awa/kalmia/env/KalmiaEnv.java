package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.resource.loader.ResourcesLoader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.env.security.exception.PreShareKeyNotFoundException;
import com.github.cao.awa.kalmia.framework.network.unsolve.UnsolvedPacketFramework;
import com.github.cao.awa.kalmia.framework.serialize.ByteSerializeFramework;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPublicKey;
import java.util.Map;
import java.util.function.Supplier;

public class KalmiaEnv {
    public static boolean setup = false;
    public static boolean isServer = true;
    public static final UnsolvedPacketFramework unsolvedFramework = new UnsolvedPacketFramework();
    public static final ByteSerializeFramework serializeFramework = new ByteSerializeFramework();
    public static final RequestProtocolName STANDARD_REQUEST_PROTOCOL = new RequestProtocolName("KALMIA_STANDARD",
                                                                                                0
    );
    public static final Map<RequestProtocolName, RequestProtocol> protocols = EntrustEnvironment.operation(ApricotCollectionFactor.newHashMap(),
                                                                                                           map -> {

                                                                                                           }
    );

    public static void setupClient() throws PreShareKeyNotFoundException {
        isServer = false;

        setupPreSharedKey();
        setupFramework();

        setup = true;
    }

    public static void setupServer() throws PreShareKeyNotFoundException {
        isServer = true;

        setupPreSharedKey();
        setupFramework();

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
        ECPublicKey kalmiaMainPrikey = nullThenThrow(EntrustEnvironment.trys(() -> {
                                                         return Crypto.decodeEcPubkey(Mathematics.toBytes(IOUtil.read(new InputStreamReader(ResourcesLoader.get("secret/kalmiagram/main/SECRET_PUBLIC"),
                                                                                                                                            StandardCharsets.UTF_8
                                                                                                          )),
                                                                                                          36
                                                         ));
                                                     }),
                                                     PreShareKeyNotFoundException :: new
        );

        KalmiaPreSharedKey.pubkeyManager.add(KalmiaPreSharedKey.defaultCipherKey,
                                             kalmiaMainPrikey
        );
    }

    public static <E extends Exception, T> T nullThenThrow(T obj, Supplier<E> exception) throws E {
        if (obj == null) {
            throw exception.get();
        }
        return obj;
    }

    public static void setupFramework() {
        unsolvedFramework.work();
        serializeFramework.work();
    }
}
