package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.SelectMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(SelectMessageEvent.class)
public interface SelectMessageEventHandler extends NetworkEventHandler<SelectMessagePacket, SelectMessageEvent> {
}
