package com.github.cao.awa.kalmia.event.handler.network.inbound.handshake.hello.client;

import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.client.ClientHelloEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;

public abstract class ClientHelloEventHandler extends NetworkEventHandler<ClientHelloPacket, ClientHelloEvent> {
}
