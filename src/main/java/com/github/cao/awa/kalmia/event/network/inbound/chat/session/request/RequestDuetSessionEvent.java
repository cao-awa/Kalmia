package com.github.cao.awa.kalmia.event.network.inbound.chat.session.request;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class RequestDuetSessionEvent extends NetworkEvent<RequestDuetSessionPacket> {
    public RequestDuetSessionEvent(RequestRouter router, RequestDuetSessionPacket packet) {
        super(router,
              packet
        );
    }
}

