package com.github.cao.awa.kalmia.translation.event.inbound.message.select;

import com.github.cao.awa.kalmia.translation.event.TranslationEvent;
import com.github.cao.awa.kalmia.translation.network.packet.message.select.TranslationSelectMessagePacket;
import com.github.cao.awa.kalmia.translation.network.router.TranslationRouter;

public class TranslationSelectMessageEvent extends TranslationEvent<TranslationSelectMessagePacket> {
    public TranslationSelectMessageEvent(TranslationRouter router, TranslationSelectMessagePacket packet) {
        super(router,
              packet
        );
    }
}