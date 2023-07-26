package com.github.cao.awa.kalmia.event.handler.network.inbound.handshake.hello.server;

import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.hello.server.ServerHelloEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;

public abstract class ServerHelloEventHandler extends NetworkEventHandler<ServerHelloPacket, ServerHelloEvent> {
}
