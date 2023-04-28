package com.github.cao.awa.kalmia.network.packet.request.message.send;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.ReceiptRequest;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see SentMessagePacket
 */
@Server
public class SentMessageRequest extends ReceiptRequest {
    public static final byte[] ID = SkippedBase256.longToBuf(11);
    private final long seq;

    public SentMessageRequest(long seq, byte[] receipt) {
        super(receipt);
        this.seq = seq;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.seq));
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
