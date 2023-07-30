package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.network.inbound.message.select.SelectedMessageEvent;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(13)
@NetworkEventTarget(SelectedMessageEvent.class)
public class SelectedMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    private long sessionId;
    @AutoData
    private long from;
    @AutoData
    private long to;
    @AutoData
    private List<Message> messages;

    @Server
    public SelectedMessagePacket(long sessionId, long from, long to, List<Message> messages) {
        this.sessionId = sessionId;
        this.from = from;
        this.to = to;
        this.messages = messages;
    }

    @Auto
    @Client
    public SelectedMessagePacket(BytesReader reader) {
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

    public List<Message> messages() {
        return this.messages;
    }
}
