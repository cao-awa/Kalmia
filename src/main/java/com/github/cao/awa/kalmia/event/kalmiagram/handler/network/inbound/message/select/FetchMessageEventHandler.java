package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.FetchMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(FetchMessageEvent.class)
public interface FetchMessageEventHandler extends NetworkEventHandler<FetchMessagePacket, FetchMessageEvent> {
}
