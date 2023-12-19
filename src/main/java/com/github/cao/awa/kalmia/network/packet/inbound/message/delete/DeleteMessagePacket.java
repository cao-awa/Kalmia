package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.delete.DeleteMessageEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 14, crypto = true)
@NetworkEventTarget(DeleteMessageEvent.class)
public class DeleteMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private PureExtraIdentity sessionIdentity;
    @AutoData
    @DoNotSet
    private long seq;

    @Auto
    @Server
    public DeleteMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public DeleteMessagePacket(PureExtraIdentity sessionIdentity, long seq) {
        this.sessionIdentity = sessionIdentity;
        this.seq = seq;
    }

    @Getter
    public PureExtraIdentity sessionIdentity() {
        return this.sessionIdentity;
    }

    @Getter
    public long seq() {
        return this.seq;
    }
}
