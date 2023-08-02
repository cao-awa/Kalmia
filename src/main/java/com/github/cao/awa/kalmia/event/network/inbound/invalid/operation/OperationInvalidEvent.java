package com.github.cao.awa.kalmia.event.network.inbound.invalid.operation;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class OperationInvalidEvent extends NetworkEvent<OperationInvalidPacket> {
    public OperationInvalidEvent(RequestRouter router, OperationInvalidPacket packet) {
        super(router,
              packet
        );
    }
}
