package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.resource.write;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write.WriteResourceNextStepEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourceNextStepPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;

public class WriteResourceNextStepEventBus extends EventBus<RequestNextResourceShardEventBusHandler> implements WriteResourceNextStepEventHandler {
    @Auto
    @Override
    public void handle(RequestRouter router, WriteResourceNextStepPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt()
        ));
    }
}
