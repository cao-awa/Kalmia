package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.SelectedMessageEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(id = 13, crypto = true)
@NetworkEventTarget(SelectedMessageEvent.class)
public class SelectedMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private PureExtraIdentity sessionIdentity;
    @AutoData
    @DoNotSet
    private long from;
    @AutoData
    @DoNotSet
    private long to;
    @AutoData
    @DoNotSet
    private long sessionCurSeq;
    @AutoData
    @DoNotSet
    private List<Message> messages;

    @Server
    public SelectedMessagePacket(PureExtraIdentity sessionIdentity, long from, long to, long sessionCurSeq, List<Message> messages) {
        this.sessionIdentity = sessionIdentity;
        this.from = from;
        this.to = to;
        this.sessionCurSeq = sessionCurSeq;
        this.messages = messages;
    }

    @Auto
    @Client
    public SelectedMessagePacket(BytesReader reader) {
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

    @Getter
    public long sessionCurSeq() {
        return this.sessionCurSeq;
    }

    @Getter
    public List<Message> messages() {
        return this.messages;
    }
}
