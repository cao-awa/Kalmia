package com.github.cao.awa.kalmia.event.handler.network.inbound.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.message.send.SentMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SentMessageEvent.class)
public interface SentMessageEventHandler extends NetworkEventHandler<SentMessagePacket, SentMessageEvent> {
}
