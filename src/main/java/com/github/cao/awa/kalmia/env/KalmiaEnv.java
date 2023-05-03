package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.resource.loader.ResourcesLoader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.kalmia.env.security.exception.PreShareKeyNotFoundException;
import com.github.cao.awa.kalmia.framework.network.unsolve.UnsolvedPacketFramework;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.cao.awa.kalmia.security.PreSharedRsaCipher;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.function.Supplier;

public class KalmiaEnv {
    public static boolean setup = false;
    public static boolean isServer = true;
    public static final UnsolvedPacketFramework unsolvedFramework = new UnsolvedPacketFramework();
    public static final RequestProtocolName STANDARD_REQUEST_PROTOCOL = new RequestProtocolName("KALMIA_STANDARD",
                                                                                                0
    );
    public static final Map<RequestProtocolName, RequestProtocol> protocols = EntrustEnvironment.operation(ApricotCollectionFactor.newHashMap(),
                                                                                                           map -> {

                                                                                                           }
    );
    public static String defaultCipherKey = "Kalmia/Main";
    public static String expectCipherKey = defaultCipherKey;
    public static Map<String, RSAPrivateKey> DEFAULT_PRE_PRIKEY = ApricotCollectionFactor.newHashMap();
    public static Map<String, PreSharedRsaCipher> DEFAULT_PRE_PUBKEY = ApricotCollectionFactor.newHashMap();

    public static void setupClient() throws PreShareKeyNotFoundException {
        isServer = false;

        setupPreSharedKey();
        setupUnsolvedFramework();

        setup = true;
    }

    public static void setupServer() throws PreShareKeyNotFoundException {
        isServer = true;

        setupPreSharedKey();
        setupUnsolvedFramework();

        setup = true;
    }

    public static void setupPreSharedKey() throws PreShareKeyNotFoundException {
        // Server prikey init.
        if (isServer) {
            DEFAULT_PRE_PRIKEY.put(defaultCipherKey,
                                   nullThenThrow(EntrustEnvironment.trys(() -> {
                                                     return Crypto.decodeRsaPrikey(Mathematics.toBytes(IOUtil.read(new InputStreamReader(ResourcesLoader.get("secret/kalmiagram/main/SECRET_PRIVATE"),
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
        PreSharedRsaCipher kalmiaMainPrikey = nullThenThrow(EntrustEnvironment.trys(() -> {
                                                                return new PreSharedRsaCipher(Crypto.decodeRsaPubkey(Mathematics.toBytes(IOUtil.read(new InputStreamReader(ResourcesLoader.get("secret/kalmiagram/main/SECRET_PUBLIC"),
                                                                                                                                                                           StandardCharsets.UTF_8
                                                                                                                                         )),
                                                                                                                                         36
                                                                )),
                                                                                              defaultCipherKey
                                                                );
                                                            }),
                                                            PreShareKeyNotFoundException :: new
        );

        DEFAULT_PRE_PUBKEY.put(kalmiaMainPrikey.key(),
                               kalmiaMainPrikey
        );
    }

    public static <E extends Exception, T> T nullThenThrow(T obj, Supplier<E> exception) throws E {
        if (obj == null) {
            throw exception.get();
        }
        return obj;
    }

    public static void setupUnsolvedFramework() {
        unsolvedFramework.work();
    }
}
