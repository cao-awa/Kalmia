package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.resource.loader.ResourcesLoader;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.apricot.util.io.IOUtil;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.env.security.exception.PreShareKeyNotFoundException;
import com.github.cao.awa.kalmia.framework.event.EventFramework;
import com.github.cao.awa.kalmia.framework.network.event.NetworkEventFramework;
import com.github.cao.awa.kalmia.framework.network.unsolve.PacketSerializeFramework;
import com.github.cao.awa.kalmia.framework.plugin.PluginFramework;
import com.github.cao.awa.kalmia.framework.serialize.ByteSerializeFramework;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.user.DefaultUser;
import com.github.cao.awa.kalmia.user.key.ec.EcServerKeyPair;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPublicKey;
import java.util.function.Supplier;

public class KalmiaEnv {
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
                               "123456",
                               new EcServerKeyPair(
                                       Mathematics.toBytes(
                                               "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vno5s6a3805phsxo2h3alhfv4j82g4rmnma75r2z455u3q3vwa6bnnhhuasjqn7t6ej56cg6kv4x21osmu14m09om1ofhzb1xqf6d69z8dnophc8o34h0az66852dfa8yth8oxz2t6yf3c0lhjhxbpml2dh0y5yg7ueendizsw3dslncr2ur6edjix8fnaifeygo6dhdcj5hd6",
                                               36
                                       ),
                                       Crypto.aesEncrypt(
                                               Mathematics.toBytes(
                                                       "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq360weg0c0t5vwpgs8tbpxngxghfsebzbnnyjs84lq09jlz549yznbms2axywvzbbl4ah7vzkgxknv0f2rmlvjw0i8hhzkkbhflbk3z24scim36jx3exfl87qh1m876ckxf4ixw0vcgacr9zjdhnh35gyv0mhme6k77ee15uz504gbioal8oq493vu6ucfg95nfir7kzfze3oczf2gn00ur9t8gaalmcdcrfw2b9i336sqltk996voz4nj4pfn2muutcmctl9j2uk3kjzsi40w9xo2mk6d5siu9n0czmvxoyc0f7zbtau9qtf9k8t5p57fyaxy74eiofe",
                                                       36
                                               ),
                                               testUer1AesCipher
                                       )
                               )
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
                               "123456",
                               new EcServerKeyPair(
                                       Mathematics.toBytes(
                                               "13mhgvwznod324bdi48k3x43q5q0sip185x5nh8vnnwq19j63xhbkp6z60qmcdj2z28zwce465oiv9vpwkhkqdyeibxus1xui5tdljkef2xcb121bb68c6vcsm8g3jlipz5ok13ygifen7s13lhyb9nm11nr6qg80oifk7ahlrj0ta0jek0lcocgv4oaxwoutza2pvq3k927l232tkfveyfiz0sa7jod4ph2wqdqy50vu80spoqbj",
                                               36
                                       ),
                                       Crypto.aesEncrypt(
                                               Mathematics.toBytes(
                                                       "40wrnfh37yhbkae8z9760fl3pzsdf5aj7odzrnbuttdpx0t0ykfx2yq2eypp3dstdehqpd4m5efztu0ybkzru0ze47024geehf8o9wr0u3tfqqjv6lrdbn2yphsfl3tox8i0wlswyjz0zmn5mhyj9x26zqa6ydrvwaxeyoh3963unm75jxdg3wqtpx6ckeoghwbigutg0cbicq6o3ndjdkf7fz1nx9igrdgmvyj2b00s02nyor7qh3k1ebvxmib30yksqjq4h65kqw8x4kpyamq8of75ppmssfg7u3xusg525i44undo2emciy30qbjj9lkw0at83x6nozotbdgnfvaccpn7v2atys9manaaa9baz5k6nwu90ekoxsg2q18hikf",
                                                       36
                                               ),
                                               testUer2AesCipher
                                       )
                               )
        );
    });

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
            KalmiaPreSharedCipher.prikeyManager.add(KalmiaPreSharedCipher.defaultCipherField,
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

        KalmiaPreSharedCipher.pubkeyManager.add(KalmiaPreSharedCipher.defaultCipherField,
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
