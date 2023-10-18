package com.github.cao.awa.kalmia.event.kalmiagram.handler.network;

import com.github.cao.awa.kalmia.annotation.inaction.DoNotOverride;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public interface NetworkEventHandler<P extends Packet<?>, E extends NetworkEvent<P>> extends EventHandler<E> {
    void handle(RequestRouter router, P packet);

    @Override
    @DoNotOverride
    default void handle(E event) {
        handle(
                event.router(),
                event.packet()
        );
    }
}
