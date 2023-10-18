package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SentMessageEvent extends NetworkEvent<SentMessagePacket> {
    public SentMessageEvent(RequestRouter router, SentMessagePacket packet) {
        super(router,
              packet
        );
    }
}
