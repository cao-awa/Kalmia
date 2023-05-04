package com.github.cao.awa.kalmia.network.packet.dual;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReceiptRequest;
import com.github.cao.awa.kalmia.network.packet.Request;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Arrays;

public abstract class DualPacket<T extends PacketHandler<T>> {
    private byte[] receipt = Request.RECEIPT;

    public DualPacket(byte[] receipt) {
        this.receipt = check(receipt);
    }

    public DualPacket() {

    }

    public byte[] receipt() {
        if (this.receipt == Request.RECEIPT) {
            return this.receipt;
        }
        return BytesUtil.concat(new byte[]{1},
                                this.receipt
        );
    }

    public static byte[] check(byte[] receipt) {
        if (Arrays.equals(Request.RECEIPT,
                          receipt
        )) {
            return receipt;
        }
        if (receipt.length != 16) {
            throw new IllegalArgumentException("Receipt data only allowed 16 bytes");
        }
        return receipt;
    }

    public static byte[] read(BytesReader reader) {
        return reader.read(16);
    }

    public abstract byte[] data();

    public byte[] id() {
        return KalmiaEnv.unsolvedFramework.id(EntrustEnvironment.cast(this.getClass()));
    }

    public byte[] encode(RequestRouter router) {
        return router.encode(BytesUtil.concat(id(),
                                              receipt(),
                                              data()
        ));
    }

    public abstract void inbound(RequestRouter router, T handler);

    public <X extends DualPacket<T>> X receipt(byte[] receipt) {
        this.receipt = ReceiptRequest.check(receipt);
        return EntrustEnvironment.cast(this);
    }
}
