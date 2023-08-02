package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.crypto.aes.HandshakeAesCipherEvent;
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
