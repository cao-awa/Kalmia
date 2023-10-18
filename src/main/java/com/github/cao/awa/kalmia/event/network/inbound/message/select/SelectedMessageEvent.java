package com.github.cao.awa.kalmia.event.network.inbound.message.select;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SelectedMessageEvent extends NetworkEvent<SelectedMessagePacket> {
    public SelectedMessageEvent(RequestRouter router, SelectedMessagePacket packet) {
        super(router,
              packet
        );
    }
}
