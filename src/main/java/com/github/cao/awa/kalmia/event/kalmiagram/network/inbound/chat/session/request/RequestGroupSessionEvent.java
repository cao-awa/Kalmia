package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.request;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestGroupSessionPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class RequestGroupSessionEvent extends NetworkEvent<RequestGroupSessionPacket> {
    public RequestGroupSessionEvent(RequestRouter router, RequestGroupSessionPacket packet) {
        super(router,
              packet
        );
    }
}

