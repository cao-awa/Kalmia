package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.kalmia.framework.network.unsolve.UnsolvedPacketFramework;
import com.github.cao.awa.kalmia.protocol.RequestProtocol;

public class KalmiaEnv {
    public static boolean setup = false;
    public static boolean isServer = true;
    public static final UnsolvedPacketFramework unsolvedFramework = new UnsolvedPacketFramework();
    public static final RequestProtocol STANDARD_REQUEST_PROTOCOL = new RequestProtocol("KALMIA_STANDARD",
                                                                                        0
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
