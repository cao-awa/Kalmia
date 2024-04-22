package com.github.cao.awa.kalmia.translation.event.inbound.login.password;

import com.github.cao.awa.kalmia.translation.event.TranslationEvent;
import com.github.cao.awa.kalmia.translation.network.packet.login.TranslationLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.translation.network.router.TranslationRouter;

public class TranslationLoginWithPasswordEvent extends TranslationEvent<TranslationLoginWithPasswordPacket> {
    public TranslationLoginWithPasswordEvent(TranslationRouter router, TranslationLoginWithPasswordPacket packet) {
        super(router,
              packet
        );
    }
}