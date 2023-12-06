package com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.pubkey.select;

import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select.SelectPublicKeyPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class SelectPublicKeyEvent extends NetworkEvent<SelectPublicKeyPacket> {
    public SelectPublicKeyEvent(RequestRouter router, SelectPublicKeyPacket packet) {
        super(router,
              packet
        );
    }
}
