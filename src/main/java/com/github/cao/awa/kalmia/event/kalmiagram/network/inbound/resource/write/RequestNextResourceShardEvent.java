package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.resource.write;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.RequestNextResourceShardPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class RequestNextResourceShardEvent extends NetworkEvent<RequestNextResourceShardPacket> {
    public RequestNextResourceShardEvent(RequestRouter router, RequestNextResourceShardPacket packet) {
        super(router,
              packet
        );
    }
}
