package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

public abstract class ReadonlyPacket<T extends PacketHandler<T>> extends Packet {
    private byte[] receipt = Request.RECEIPT;

    public abstract void inbound(RequestRouter router, T handler);

    public <X extends ReadonlyPacket<T>> X receipt(byte[] receipt) {
        this.receipt = ReceiptRequest.check(receipt);
        return EntrustEnvironment.cast(this);
    }

    public byte[] receipt() {
        return this.receipt;
    }
}
