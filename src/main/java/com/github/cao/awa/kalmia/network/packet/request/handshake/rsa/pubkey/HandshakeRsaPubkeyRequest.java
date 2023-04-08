package com.github.cao.awa.kalmia.network.packet.request.handshake.rsa.pubkey;

import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class HandshakeRsaPubkeyRequest extends WritablePacket {
    private static final byte[] ID = Base256.longToBuf(1);
    @Override
    public byte[] data() {
        return BytesUtil.concat(ID, new byte[]{123,123,44, 111});
    }
}
