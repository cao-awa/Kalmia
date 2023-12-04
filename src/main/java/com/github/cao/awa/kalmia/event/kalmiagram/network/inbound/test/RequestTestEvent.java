package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.test;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.test.RequestTestPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class RequestTestEvent extends NetworkEvent<RequestTestPacket> {
    public RequestTestEvent(RequestRouter router, RequestTestPacket packet) {
        super(router,
              packet
        );
    }
}
