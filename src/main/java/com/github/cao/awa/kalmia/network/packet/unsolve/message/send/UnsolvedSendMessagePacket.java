package com.github.cao.awa.kalmia.network.packet.unsolve.message.send;

import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;

public class UnsolvedSendMessagePacket extends UnsolvedPacket<SendMessagePacket> {
    public UnsolvedSendMessagePacket(byte[] data) {
        super(data);
    }

    @Override
    public SendMessagePacket packet() {
        return SendMessagePacket.create(reader());
    }
}
