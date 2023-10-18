package com.github.cao.awa.kalmia.event.translation.handler;

import com.github.cao.awa.kalmia.annotation.inaction.DoNotOverride;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler;
import com.github.cao.awa.kalmia.event.translation.TranslationEvent;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;

public interface TranslationEventHandler<P extends TranslationPacket, E extends TranslationEvent<P>> extends EventHandler<E> {
    void handle(TranslationRouter router, P packet);

    @Override
    @DoNotOverride
    default void handle(E event) {
        handle(
                event.router(),
                event.packet()
        );
    }
}
