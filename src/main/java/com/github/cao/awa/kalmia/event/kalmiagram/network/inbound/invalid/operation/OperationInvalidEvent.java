package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.invalid.operation;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class OperationInvalidEvent extends NetworkEvent<OperationInvalidPacket> {
    public OperationInvalidEvent(RequestRouter router, OperationInvalidPacket packet) {
        super(router,
              packet
        );
    }
}
