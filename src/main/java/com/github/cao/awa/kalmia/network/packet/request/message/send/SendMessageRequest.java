package com.github.cao.awa.kalmia.network.packet.request.message.send;

import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.ReceiptRequest;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

/**
 * @see SendMessagePacket
 */
@Client
public class SendMessageRequest extends ReceiptRequest {
    public static final byte[] ID = SkippedBase256.longToBuf(10);
    private final long sessionId;
    private final byte[] msg;

    public SendMessageRequest(long sessionId, byte[] msg, byte[] receipt) {
        super(receipt);
        this.sessionId = sessionId;
        this.msg = msg;
    }

    @Override
    public byte[] data() {
        return BytesUtil.concat(SkippedBase256.longToBuf(this.sessionId),
                                this.msg
        );
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
