package com.github.cao.awa.kalmia.network.packet.inbound.resource.write;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.resource.write.WriteResourceEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;

@AutoSolvedPacket(id = 300000, crypto = true)
@NetworkEventTarget(WriteResourceEvent.class)
public class WriteResourcePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private byte[] data;
    @AutoData
    @DoNotSet
    private boolean isFinal;

    public WriteResourcePacket(byte[] data, boolean isFinal) {
        this.data = data;
        this.isFinal = isFinal;
    }

    @Auto
    public WriteResourcePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public byte[] data() {
        return this.data;
    }

    @Getter
    public boolean isFinal() {
        return this.isFinal;
    }
}
