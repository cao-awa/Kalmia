package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

public abstract class ReadonlyPacket<T extends PacketHandler<?, T>> extends Packet {
    public abstract void inbound(UnsolvedRequestRouter router, T handler);
}
