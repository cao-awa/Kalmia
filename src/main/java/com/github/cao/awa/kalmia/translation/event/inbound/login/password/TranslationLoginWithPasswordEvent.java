package com.github.cao.awa.kalmia.translation.event.inbound.login.password;

import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.event.TranslationEvent;
import com.github.cao.awa.kalmia.translation.network.packet.login.TranslationLoginWithPasswordPacket;

public class TranslationLoginWithPasswordEvent extends TranslationEvent<TranslationLoginWithPasswordPacket> {
    public TranslationLoginWithPasswordEvent(TranslationRouter router, TranslationLoginWithPasswordPacket packet) {
        super(router,
              packet
        );
    }
}