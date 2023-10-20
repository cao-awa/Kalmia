package com.github.cao.awa.kalmia.event.translation.handler.inbound;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.translation.handler.TranslationEventHandler;
import com.github.cao.awa.kalmia.event.translation.inbound.TranslationProxyConnectEvent;
import com.github.cao.awa.kalmia.translation.network.packet.meta.connect.TranslationProxyConnectPacket;

@Auto
@AutoHandler(TranslationProxyConnectEvent.class)
public interface TranslationProxyConnectEventHandler extends TranslationEventHandler<TranslationProxyConnectPacket, TranslationProxyConnectEvent> {
}
