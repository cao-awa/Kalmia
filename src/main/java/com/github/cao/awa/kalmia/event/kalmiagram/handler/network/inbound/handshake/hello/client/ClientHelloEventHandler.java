package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.hello.client;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.hello.client.ClientHelloEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(ClientHelloEvent.class)
public interface ClientHelloEventHandler extends NetworkEventHandler<ClientHelloPacket, ClientHelloEvent> {
}
