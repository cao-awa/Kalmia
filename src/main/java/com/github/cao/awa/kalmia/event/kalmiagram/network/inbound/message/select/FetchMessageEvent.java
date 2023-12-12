package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class FetchMessageEvent extends NetworkEvent<FetchMessagePacket> {
    public FetchMessageEvent(RequestRouter router, FetchMessagePacket packet) {
        super(router,
              packet
        );
    }
}
