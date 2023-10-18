package com.github.cao.awa.kalmia.event.translation.inbound;

import com.github.cao.awa.kalmia.event.translation.TranslationEvent;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.network.packet.meta.TranslationProxyConnectPacket;

public class TranslationProxyConnectEvent extends TranslationEvent<TranslationProxyConnectPacket> {
    public TranslationProxyConnectEvent(TranslationRouter router, TranslationProxyConnectPacket packet) {
        super(router,
              packet
        );
    }
}
