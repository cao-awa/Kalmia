package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.resource.write;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write.WriteResourceEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourcePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;

public class WriteResourceEventBus extends EventBus<WriteResourceEventBusHandler> implements WriteResourceEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, WriteResourcePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.data(),
                                          packet.isFinal()
        ));
    }
}
