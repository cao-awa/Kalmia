package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.crypto.ec.pubkey;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(HandshakePreSharedEcEvent.class)
public interface HandshakePreSharedEcEventHandler extends NetworkEventHandler<HandshakePreSharedEcPacket, HandshakePreSharedEcEvent> {
}
