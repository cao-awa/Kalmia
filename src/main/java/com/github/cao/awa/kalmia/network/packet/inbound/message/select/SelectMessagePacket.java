package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.SelectMessageEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 12, crypto = true)
@NetworkEventTarget(SelectMessageEvent.class)
public class SelectMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private PureExtraIdentity sessionIdentity;
    @AutoData
    @DoNotSet
    private long from;
    @AutoData
    @DoNotSet
    private long to;

    @Client
    public SelectMessagePacket(PureExtraIdentity sessionIdentity, long from, long to) {
        this.sessionIdentity = sessionIdentity;
        this.from = from;
        this.to = to;
    }

    @Auto
    @Server
    public SelectMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public PureExtraIdentity sessionIdentity() {
        return this.sessionIdentity;
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
