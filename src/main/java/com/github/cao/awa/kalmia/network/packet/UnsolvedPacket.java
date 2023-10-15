package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;

public abstract class UnsolvedPacket<T extends Packet<?>> {
    private final byte[] data;
    private byte[] receipt = Packet.RECEIPT;

    public byte[] data() {
        return this.data;
    }

    public BytesReader reader() {
        return BytesReader.of(this.data);
    }

    public UnsolvedPacket(byte[] data) {
        this.data = data;
    }

    public final UnsolvedPacket<T> receipt(byte[] receipt) {
        this.receipt = receipt;
        return this;
    }

    public final byte[] receipt() {
        return this.receipt;
    }

    public abstract T packet();

    public boolean isStateless() {
        return false;
    }
}
