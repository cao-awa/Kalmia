package com.github.cao.awa.kalmia.translation.event.handler.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.translation.event.handler.TranslationEventHandler;
import com.github.cao.awa.kalmia.translation.event.inbound.message.select.TranslationSelectMessageEvent;
import com.github.cao.awa.kalmia.translation.network.packet.message.select.TranslationSelectMessagePacket;

@Auto
@AutoHandler(TranslationSelectMessageEvent.class)
public interface TranslationSelectMessageEventHandler extends TranslationEventHandler<TranslationSelectMessagePacket, TranslationSelectMessageEvent> {
}
