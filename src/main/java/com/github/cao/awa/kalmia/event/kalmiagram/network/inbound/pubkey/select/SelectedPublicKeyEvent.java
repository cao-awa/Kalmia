package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.pubkey.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select.SelectedPublicKeyPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SelectedPublicKeyEvent extends NetworkEvent<SelectedPublicKeyPacket> {
    public SelectedPublicKeyEvent(RequestRouter router, SelectedPublicKeyPacket packet) {
        super(router,
              packet
        );
    }
}
