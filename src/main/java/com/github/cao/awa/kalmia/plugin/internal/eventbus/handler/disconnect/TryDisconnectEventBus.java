package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.disconnect;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.disconnect.TryDisconnectEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;

public class TryDisconnectEventBus extends EventBus<TryDisconnectEventBusHandler> implements TryDisconnectEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, TryDisconnectPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.reason()
        ));
    }
}
