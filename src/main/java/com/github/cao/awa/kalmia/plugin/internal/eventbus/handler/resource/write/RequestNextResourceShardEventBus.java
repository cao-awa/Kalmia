package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.resource.write;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write.RequestNextResourceShardEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.RequestNextResourceShardPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;

public class RequestNextResourceShardEventBus extends EventBus<RequestNextResourceShardEventBusHandler> implements RequestNextResourceShardEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, RequestNextResourceShardPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt()
        ));
    }
}
