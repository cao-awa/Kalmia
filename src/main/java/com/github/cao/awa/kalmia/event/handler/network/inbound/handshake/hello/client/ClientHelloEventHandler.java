package com.github.cao.awa.kalmia.event.handler.network.inbound.handshake.hello.client;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.client.ClientHelloEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(ClientHelloEvent.class)
public interface ClientHelloEventHandler extends NetworkEventHandler<ClientHelloPacket, ClientHelloEvent> {
}
