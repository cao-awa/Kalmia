package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Arrays;

public abstract class Packet<T extends PacketHandler<T>> {
    public static final byte[] RECEIPT = new byte[]{- 1};
    private byte[] receipt = RECEIPT;

    public Packet(byte[] receipt) {
        this.receipt = check(receipt);
    }

    public Packet() {

    }

    public byte[] receipt() {
        if (this.receipt == RECEIPT) {
            return this.receipt;
        }
        return BytesUtil.concat(new byte[]{1},
                                this.receipt
        );
    }

    public static byte[] check(byte[] receipt) {
        if (Arrays.equals(RECEIPT,
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

    public <X extends Packet<T>> X receipt(byte[] receipt) {
        this.receipt = check(receipt);
        return EntrustEnvironment.cast(this);
    }
}
