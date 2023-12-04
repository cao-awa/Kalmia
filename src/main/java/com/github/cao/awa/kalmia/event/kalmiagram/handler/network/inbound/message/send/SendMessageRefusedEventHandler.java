package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.send;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send.SendMessageRefusedEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessageRefusedPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SendMessageRefusedEvent.class)
public interface SendMessageRefusedEventHandler extends NetworkEventHandler<SendMessageRefusedPacket, SendMessageRefusedEvent> {
}
