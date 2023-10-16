package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.network.inbound.message.send.SendMessageEvent;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 100)
@NetworkEventTarget(SendMessageEvent.class)
public class SendMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long sessionId;
    @AutoData
    @DoNotSet
    private Message msg;

    @Auto
    @Server
    public SendMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Client
    public SendMessagePacket(long sessionId, Message msg, byte[] receipt) {
        super(receipt);
        this.sessionId = sessionId;
        this.msg = msg;
    }

    @Getter
    public long sessionId() {
        return this.sessionId;
    }

    public Message msg() {
        return this.msg;
    }
}
