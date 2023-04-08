package com.github.cao.awa.kalmia;

import com.github.cao.awa.kalmia.network.io.KalmiaServerNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;

public class KalmiaServer {
    private final KalmiaServerNetworkIo networkIo;

    public KalmiaServer() {
        this.networkIo = new KalmiaServerNetworkIo(this);
    }

    public void startup() throws Exception {
        setupNetwork();
    }

    public void setupNetwork() throws Exception {
        UnsolvedPacketFactor.register();

        this.networkIo.start(12345);
    }

    public boolean useEpoll() {
        return true;
    }
}