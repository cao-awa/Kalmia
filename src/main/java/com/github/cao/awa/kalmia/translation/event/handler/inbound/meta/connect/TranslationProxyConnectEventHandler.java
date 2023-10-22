package com.github.cao.awa.kalmia.translation.event.handler.inbound.meta.connect;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.translation.event.handler.TranslationEventHandler;
import com.github.cao.awa.kalmia.translation.event.inbound.meta.connect.TranslationProxyConnectEvent;
import com.github.cao.awa.kalmia.translation.network.packet.meta.connect.TranslationProxyConnectPacket;

@Auto
@AutoHandler(TranslationProxyConnectEvent.class)
public interface TranslationProxyConnectEventHandler extends TranslationEventHandler<TranslationProxyConnectPacket, TranslationProxyConnectEvent> {
}
