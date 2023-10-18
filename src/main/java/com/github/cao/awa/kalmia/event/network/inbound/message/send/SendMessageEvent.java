package com.github.cao.awa.kalmia.event.network.inbound.message.send;

import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SendMessageEventHandler;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

/**
 * @see SendMessageEventHandler
 */
public class SendMessageEvent extends NetworkEvent<SendMessagePacket> {
    public SendMessageEvent(RequestRouter router, SendMessagePacket packet) {
        super(router,
              packet
        );
    }
}
