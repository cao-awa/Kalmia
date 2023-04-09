package com.github.cao.awa.kalmia.network.packet.request.handshake.hello;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.handshake.hello.ClientHelloPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see ClientHelloPacket
 */
@Client
public class ClientHelloRequest extends WritablePacket {
    private static final byte[] ID = SkippedBase256.longToBuf(0);

    @Override
    public byte[] data() {
        // Status packet, do not write any data here.
        return BytesUtil.EMPTY;
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
