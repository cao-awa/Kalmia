package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.crypto.aes.HandshakeAesCipherEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes.HandshakeAesCipherPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(HandshakeAesCipherEvent.class)
public interface HandshakeAesCipherEventHandler extends NetworkEventHandler<HandshakeAesCipherPacket, HandshakeAesCipherEvent> {
}
