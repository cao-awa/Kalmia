package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SelectMessageEvent extends NetworkEvent<SelectMessagePacket> {
    public SelectMessageEvent(RequestRouter router, SelectMessagePacket packet) {
        super(router,
              packet
        );
    }
}
