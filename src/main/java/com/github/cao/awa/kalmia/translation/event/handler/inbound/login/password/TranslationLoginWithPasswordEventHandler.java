package com.github.cao.awa.kalmia.translation.event.handler.inbound.login.password;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.translation.event.handler.TranslationEventHandler;
import com.github.cao.awa.kalmia.translation.event.inbound.login.password.TranslationLoginWithPasswordEvent;
import com.github.cao.awa.kalmia.translation.network.packet.login.TranslationLoginWithPasswordPacket;

@Auto
@AutoHandler(TranslationLoginWithPasswordEvent.class)
public interface TranslationLoginWithPasswordEventHandler extends TranslationEventHandler<TranslationLoginWithPasswordPacket, TranslationLoginWithPasswordEvent> {
}
