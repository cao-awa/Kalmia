package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.FetchedMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.FetchedMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(FetchedMessageEvent.class)
public interface FetchedMessageEventHandler extends NetworkEventHandler<FetchedMessagePacket, FetchedMessageEvent> {
}
