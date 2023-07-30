package com.github.cao.awa.kalmia.event.handler.network.inbound.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.message.select.SelectedMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SelectedMessageEvent.class)
public interface SelectedMessageEventHandler extends NetworkEventHandler<SelectedMessagePacket, SelectedMessageEvent> {
}
