package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SelectKeyStoreEvent extends NetworkEvent<SelectKeyStorePacket> {
    public SelectKeyStoreEvent(RequestRouter router, SelectKeyStorePacket packet) {
        super(router,
              packet
        );
    }
}
