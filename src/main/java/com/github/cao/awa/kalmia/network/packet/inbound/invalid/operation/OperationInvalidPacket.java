package com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

public class OperationInvalidPacket<T extends PacketHandler<?, T>> extends ReadonlyPacket<T> {
    @Override
    public void inbound(UnsolvedRequestRouter router, T handler) {

    }
}
