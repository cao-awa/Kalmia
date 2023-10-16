package com.github.cao.awa.kalmia.event.handler.network.inbound.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.message.send.SendMessageRefusedEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SendMessageRefusedEvent.class)
public interface SendMessageRefusedEventHandler extends NetworkEventHandler<SendMessageRefusedPacket, SendMessageRefusedEvent> {
}
