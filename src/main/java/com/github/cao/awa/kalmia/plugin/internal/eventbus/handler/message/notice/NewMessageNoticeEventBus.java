package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.notice;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.notice.NewMessageNoticeEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_eventbus")
public class NewMessageNoticeEventBus extends EventBus<NewMessageNoticeEventBusHandler> implements NewMessageNoticeEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, NewMessageNoticePacket packet) {
        trigger(handler -> handler.handle(router,
                                          packet.receipt(),
                                          packet.sessionId(),
                                          packet.seq(),
                                          packet.message()
        ));
    }
}
