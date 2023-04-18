package com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.rsa.pubkey;

import com.github.cao.awa.kalmia.network.packet.inbound.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.UnsolvedHandshakePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see HandshakeRsaPubkeyPacket
 */
@Client
public class UnsolvedHandshakeRsaPubkeyPacket extends UnsolvedHandshakePacket<HandshakeRsaPubkeyPacket> {
    public UnsolvedHandshakeRsaPubkeyPacket(byte[] data) {
        super(data);
    }

    @Override
    public HandshakeRsaPubkeyPacket packet() {
        return HandshakeRsaPubkeyPacket.create(reader());
    }
}
