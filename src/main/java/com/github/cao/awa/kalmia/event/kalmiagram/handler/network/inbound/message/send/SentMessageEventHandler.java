package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send.SentMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SentMessageEvent.class)
public interface SentMessageEventHandler extends NetworkEventHandler<SentMessagePacket, SentMessageEvent> {
}
