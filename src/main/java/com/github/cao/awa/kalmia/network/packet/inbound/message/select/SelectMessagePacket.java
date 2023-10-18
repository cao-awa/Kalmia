package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.SelectMessageEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 12)
@NetworkEventTarget(SelectMessageEvent.class)
public class SelectMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long sessionId;
    @AutoData
    @DoNotSet
    private long from;
    @AutoData
    @DoNotSet
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

    @Getter
    public long sessionId() {
        return this.sessionId;
    }

    @Getter
    public long from() {
        return this.from;
    }

    @Getter
    public long to() {
        return this.to;
    }
}
