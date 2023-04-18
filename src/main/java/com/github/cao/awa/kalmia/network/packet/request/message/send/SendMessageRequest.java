package com.github.cao.awa.kalmia.network.packet.request.message.send;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see SendMessagePacket
 */
@Client
public class SendMessageRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(10);
    private final long sessionId;
    private final byte[] identity;
    private final byte[] msg;

    public SendMessageRequest(long sessionId, byte[] identity, byte[] msg) {
        this.sessionId = sessionId;
        this.identity = identity;
        this.msg = msg;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                this.identity,
                                this.msg
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
