package com.github.cao.awa.kalmia.translation.event.inbound.meta.connect;

import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.event.TranslationEvent;
import com.github.cao.awa.kalmia.translation.network.packet.meta.connect.TranslationProxyConnectPacket;

public class TranslationProxyConnectEvent extends TranslationEvent<TranslationProxyConnectPacket> {
    public TranslationProxyConnectEvent(TranslationRouter router, TranslationProxyConnectPacket packet) {
        super(router,
              packet
        );
    }
}
