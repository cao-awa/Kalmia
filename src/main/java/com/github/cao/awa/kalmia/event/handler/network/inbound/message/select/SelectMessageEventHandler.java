package com.github.cao.awa.kalmia.event.handler.network.inbound.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.message.select.SelectMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(SelectMessageEvent.class)
public interface SelectMessageEventHandler extends NetworkEventHandler<SelectMessagePacket, SelectMessageEvent> {
}
