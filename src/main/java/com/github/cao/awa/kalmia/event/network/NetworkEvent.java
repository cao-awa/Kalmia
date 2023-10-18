package com.github.cao.awa.kalmia.event.network;

import com.github.cao.awa.kalmia.event.Event;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public abstract class NetworkEvent<T extends Packet<?>> extends Event {
    private final RequestRouter router;
    private final T packet;

    public NetworkEvent(RequestRouter router, T packet) {
        this.router = router;
        this.packet = packet;
    }

    public RequestRouter router() {
        return this.router;
    }

    public T packet() {
        return this.packet;
    }
}
