package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.ec.pubkey;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.crypto.ec.pubkey.HandshakePreSharedEcEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AutoAllData
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 1, crypto = false)
@NetworkEventTarget(HandshakePreSharedEcEvent.class)
public class HandshakePreSharedEcPacket extends Packet<HandshakeHandler> {
    private String cipherField;

    @Auto
    @Client
    public HandshakePreSharedEcPacket(BytesReader reader) {
        super(reader);
    }
}
