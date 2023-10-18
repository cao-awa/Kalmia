package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SendMessageRefusedEvent extends NetworkEvent<SendMessageRefusedPacket> {
    public SendMessageRefusedEvent(RequestRouter router, SendMessageRefusedPacket packet) {
        super(router,
              packet
        );
    }
}
