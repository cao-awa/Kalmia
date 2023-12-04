package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send.SendMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(SendMessageEvent.class)
public interface SendMessageEventHandler extends NetworkEventHandler<SendMessagePacket, SendMessageEvent> {
}
