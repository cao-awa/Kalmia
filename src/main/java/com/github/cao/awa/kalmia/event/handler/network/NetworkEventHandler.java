package com.github.cao.awa.kalmia.event.handler.network;

import com.github.cao.awa.kalmia.annotation.auto.DoNotOverride;
import com.github.cao.awa.kalmia.event.handler.EventHandler;
import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public abstract class NetworkEventHandler<T extends Packet<?>, E extends NetworkEvent<T>> extends EventHandler<E> {
    public abstract void handle(RequestRouter router, T packet);

    @Override
    @DoNotOverride
    public void handle(E event) {
        handle(event.router(),
               event.packet()
        );
    }
}
