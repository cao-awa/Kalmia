package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.crypto.aes.HandshakeAesCipherEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 2)
@NetworkEventTarget(HandshakeAesCipherEvent.class)
public class HandshakeAesCipherPacket extends Packet<HandshakeHandler> {
    @AutoData
    @DoNotSet
    private byte[] cipher;

    @Client
    public HandshakeAesCipherPacket(byte[] cipher) {
        this.cipher = cipher;
    }

    @Auto
    @Server
    public HandshakeAesCipherPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public byte[] cipher() {
        return this.cipher;
    }
}
