package com.github.cao.awa.kalmia.network.packet.request.handshake.hello;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.ServerHelloPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see ServerHelloPacket
 */
@Server
public class ServerHelloRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(3);
    private final byte[] testKey;
    private final byte[] testSha;

    public ServerHelloRequest(byte[] testKey, byte[] testSha) {
        try {
            this.testKey = testKey;
            this.testSha = testSha;
        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(Base256.tagToBuf(this.testKey.length),
                                this.testKey,
                                new byte[]{(byte) this.testSha.length},
                                this.testSha
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
