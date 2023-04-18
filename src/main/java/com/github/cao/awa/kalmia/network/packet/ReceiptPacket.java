package com.github.cao.awa.kalmia.network.packet;

public abstract class ReceiptPacket extends WritablePacket {
    private final byte[] receipt;

    public ReceiptPacket(byte[] receipt) {
        this.receipt = receipt;
    }

    public byte[] receipt() {
        return this.receipt;
    }
}
