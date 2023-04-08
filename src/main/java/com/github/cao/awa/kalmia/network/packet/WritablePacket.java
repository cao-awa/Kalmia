package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

public abstract class WritablePacket extends Packet {
    public abstract byte[] data();

    public byte[] encode(UnsolvedRequestRouter router) {
        return router.encode(data());
    }

    public byte[] decode(UnsolvedRequestRouter router) {
        return router.decode(data());
    }
}
