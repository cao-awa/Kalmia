package com.github.cao.awa.kalmia.network.packet.inbound.message.delete;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.network.inbound.message.delete.DeleteMessageEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 14)
@NetworkEventTarget(DeleteMessageEvent.class)
public class DeleteMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long sessionId;
    @AutoData
    @DoNotSet
    private long seq;

    @Auto
    @Server
    public DeleteMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public DeleteMessagePacket(long sessionId, long seq) {
        this.sessionId = sessionId;
        this.seq = seq;
    }

    @Getter
    public long sessionId() {
        return this.sessionId;
    }

    @Getter
    public long seq() {
        return this.seq;
    }
}
