package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.network.unsolve.UnsolvedPacketFramework;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;
import com.github.cao.awa.kalmia.protocol.RequestProtocolName;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Map;

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

    public static void setupClient() {
        isServer = false;

        setupUnsolvedFramework();

        setup = true;
    }

    public static void setupServer() {
        isServer = true;

        setupUnsolvedFramework();

        setup = true;
    }

    public static void setupUnsolvedFramework() {
        unsolvedFramework.work();
    }
}
