package com.github.cao.awa.kalmia.network.packet.request.invalid.operation;

import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Server
public class OperationInvalidRequest extends WritablePacket {
    @Override
    public byte[] data() {
        return new byte[0];
    }

    @Override
    public byte[] id() {
        return new byte[0];
    }
}
