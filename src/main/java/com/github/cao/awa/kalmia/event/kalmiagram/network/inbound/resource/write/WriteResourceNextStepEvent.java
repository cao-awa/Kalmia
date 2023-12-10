package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.resource.write;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourceNextStepPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class WriteResourceNextStepEvent extends NetworkEvent<WriteResourceNextStepPacket> {
    public WriteResourceNextStepEvent(RequestRouter router, WriteResourceNextStepPacket packet) {
        super(router,
              packet
        );
    }
}
