package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SessionListenersUpdateEvent extends NetworkEvent<SessionListenersUpdatePacket> {
    public SessionListenersUpdateEvent(RequestRouter router, SessionListenersUpdatePacket packet) {
        super(router,
              packet
        );
    }
}

