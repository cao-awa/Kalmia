package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class FetchedMessageEvent extends NetworkEvent<FetchedMessagePacket> {
    public FetchedMessageEvent(RequestRouter router, FetchedMessagePacket packet) {
        super(router,
              packet
        );
    }
}
