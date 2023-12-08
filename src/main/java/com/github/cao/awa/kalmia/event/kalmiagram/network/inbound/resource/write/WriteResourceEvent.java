package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.resource.write;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourcePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class WriteResourceEvent extends NetworkEvent<WriteResourcePacket> {
    public WriteResourceEvent(RequestRouter router, WriteResourcePacket packet) {
        super(router,
              packet
        );
    }
}
