package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send.SendMessageRefusedEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_eventbus")
public class SendMessageRefusedEventBus extends EventBus<SendMessageRefusedEventBusHandler> implements SendMessageRefusedEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SendMessageRefusedPacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.reason()
        ));
    }
}
