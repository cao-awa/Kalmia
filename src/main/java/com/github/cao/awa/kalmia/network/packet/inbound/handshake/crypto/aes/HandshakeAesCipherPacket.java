package com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.aes;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.handshake.crypto.aes.HandshakeAesCipherEvent;
import com.github.cao.awa.kalmia.network.handler.handshake.HandshakeHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@AutoAllData
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 2, crypto = false)
@NetworkEventTarget(HandshakeAesCipherEvent.class)
public class HandshakeAesCipherPacket extends Packet<HandshakeHandler> {
    private byte[] cipher;

    @Auto
    @Server
    public HandshakeAesCipherPacket(BytesReader reader) {
        super(reader);
    }
}
