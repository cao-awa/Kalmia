package com.github.cao.awa.kalmia.event.network.inbound.message.delete;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class DeletedMessageEvent extends NetworkEvent<DeletedMessagePacket> {
    public DeletedMessageEvent(RequestRouter router, DeletedMessagePacket packet) {
        super(router,
              packet
        );
    }
}
