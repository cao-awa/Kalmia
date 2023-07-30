package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(1)
@NetworkEventTarget(HandshakePreSharedEcEvent.class)
public class HandshakePreSharedEcPacket extends Packet<HandshakeHandler> {
    @AutoData
    private String cipherField;

    @Server
    public HandshakePreSharedEcPacket(String cipherField) {
        this.cipherField = cipherField;
    }

    @Auto
    @Client
    public HandshakePreSharedEcPacket(BytesReader reader) {
        super(reader);
    }

    public String cipherField() {
        return this.cipherField;
    }
}
