package com.github.cao.awa.kalmia.event.network.inbound.message.delete;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeleteMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class DeleteMessageEvent extends NetworkEvent<DeleteMessagePacket> {
    public DeleteMessageEvent(RequestRouter router, DeleteMessagePacket packet) {
        super(router,
              packet
        );
    }
}
