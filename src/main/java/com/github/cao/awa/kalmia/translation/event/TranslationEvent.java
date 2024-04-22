package com.github.cao.awa.kalmia.translation.event;

import com.github.cao.awa.kalmia.event.Event;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.kalmia.translation.network.router.TranslationRouter;

public class TranslationEvent<T extends TranslationPacket> extends Event {
    private final TranslationRouter router;
    private final T packet;

    public TranslationEvent(TranslationRouter router, T packet) {
        this.router = router;
        this.packet = packet;
    }

    public TranslationRouter router() {
        return this.router;
    }

    public T packet() {
        return this.packet;
    }
}
