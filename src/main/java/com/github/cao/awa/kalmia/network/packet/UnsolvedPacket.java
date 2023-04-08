package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;

public abstract class UnsolvedPacket<T extends ReadonlyPacket> extends Packet {
    private final byte[] data;

    public byte[] data() {
        return this.data;
    }

    public BytesReader reader() {
        return new BytesReader(this.data);
    }

    public UnsolvedPacket(byte[] data) {
        this.data = data;
    }

    public abstract T toPacket();
}
