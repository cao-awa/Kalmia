package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.SelectedMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectedMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SelectedMessageEvent.class)
public interface SelectedMessageEventHandler extends NetworkEventHandler<SelectedMessagePacket, SelectedMessageEvent> {
}
