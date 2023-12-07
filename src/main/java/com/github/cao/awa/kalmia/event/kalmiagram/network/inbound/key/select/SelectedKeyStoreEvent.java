package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectedKeyStorePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SelectedKeyStoreEvent extends NetworkEvent<SelectedKeyStorePacket> {
    public SelectedKeyStoreEvent(RequestRouter router, SelectedKeyStorePacket packet) {
        super(router,
              packet
        );
    }
}
