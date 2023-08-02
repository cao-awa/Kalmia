package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.network.inbound.message.send.SentMessageEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 11)
@NetworkEventTarget(SentMessageEvent.class)
public class SentMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long seq;

    @Server
    public SentMessagePacket(long seq, byte[] receipt) {
        super(receipt);
        this.seq = seq;
    }

    @Auto
    @Client
    public SentMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public long seq() {
        return this.seq;
    }
}
