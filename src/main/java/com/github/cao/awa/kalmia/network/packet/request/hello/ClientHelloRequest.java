package com.github.cao.awa.kalmia.network.packet.request.hello;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class ClientHelloRequest extends WritablePacket {
    private static final byte[] ID = Base256.longToBuf(0);

    @Override
    public byte[] data() {
        return BytesUtil.concat(ID, "CLIENT HELLO".getBytes());
    }
}
