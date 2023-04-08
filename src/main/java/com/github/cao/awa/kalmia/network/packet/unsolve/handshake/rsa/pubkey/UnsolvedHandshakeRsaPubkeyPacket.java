package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.rsa.pubkey;

import com.github.cao.awa.kalmia.network.packet.handshake.rsa.pubkey.HandshakeRsaPubkeyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;

public class UnsolvedHandshakeRsaPubkeyPacket extends UnsolvedHandshakePacket<HandshakeRsaPubkeyPacket> {
    public UnsolvedHandshakeRsaPubkeyPacket(byte[] data) {
        super(data);
    }

    @Override
    public HandshakeRsaPubkeyPacket toPacket() {
        return HandshakeRsaPubkeyPacket.create(reader());
    }
}
