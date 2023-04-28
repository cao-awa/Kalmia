package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public abstract class ReceiptRequest extends Request {
    private final byte[] receipt;

    public ReceiptRequest(byte[] receipt) {
        this.receipt = check(receipt);
    }

    public byte[] receipt() {
        return BytesUtil.concat(new byte[]{1},
                                this.receipt
        );
    }

    public static byte[] check(byte[] receipt) {
        if (receipt.length != 16) {
            throw new IllegalArgumentException("Receipt data only allowed 16 bytes");
        }
        return receipt;
    }

    public static byte[] read(BytesReader reader) {
        return reader.read(16);
    }
}
