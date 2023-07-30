package com.github.cao.awa.kalmia.event.network.inbound.message.select;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class SelectMessageEvent extends NetworkEvent<SelectMessagePacket> {
    public SelectMessageEvent(RequestRouter router, SelectMessagePacket packet) {
        super(router,
              packet
        );
    }
}
