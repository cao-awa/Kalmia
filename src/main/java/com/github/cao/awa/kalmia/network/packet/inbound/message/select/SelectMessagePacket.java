package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.network.inbound.message.select.SelectMessageEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(12)
@NetworkEventTarget(SelectMessageEvent.class)
public class SelectMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long sessionId;
    @AutoData
    private long from;
    @AutoData
    private long to;

    @Client
    public SelectMessagePacket(long sessionId, long from, long to) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
    }

    @Auto
    @Server
    public SelectMessagePacket(BytesReader reader) {
        super(reader);
    }

    public long sessionId() {
        return this.sessionId;
    }

    public long from() {
        return this.from;
    }

    public long to() {
        return this.to;
    }
}
