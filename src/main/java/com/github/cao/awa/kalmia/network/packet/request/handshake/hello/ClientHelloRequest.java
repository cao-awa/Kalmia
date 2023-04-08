package com.github.cao.awa.kalmia.network.packet.request.handshake.hello;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
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
        String helloInfo = "CLIENT HELLO";

        return BytesUtil.concat(Base256.tagToBuf(helloInfo.length()),
                                helloInfo.getBytes()
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
