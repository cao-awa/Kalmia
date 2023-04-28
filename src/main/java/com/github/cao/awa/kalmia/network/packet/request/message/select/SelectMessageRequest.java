package com.github.cao.awa.kalmia.network.packet.request.message.select;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see SelectMessagePacket
 */
@Client
public class SelectMessageRequest extends Request {
    public static final byte[] ID = SkippedBase256.longToBuf(12);
    private final long sessionId;
    private final long from;
    private final long to;

    public SelectMessageRequest(long sessionId, long from, long to) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                SkippedBase256.longToBuf(this.from),
                                SkippedBase256.longToBuf(this.to)
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
