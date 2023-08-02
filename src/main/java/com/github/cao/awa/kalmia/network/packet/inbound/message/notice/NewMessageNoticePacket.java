package com.github.cao.awa.kalmia.network.packet.inbound.message.notice;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.network.inbound.message.notice.NewMessageNoticeEvent;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 200000)
@NetworkEventTarget(NewMessageNoticeEvent.class)
public class NewMessageNoticePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long sessionId;
    @AutoData
    @DoNotSet
    private long seq;
    @AutoData
    @DoNotSet
    private Message message;

    @Server
    public NewMessageNoticePacket(long sessionId, long seq, Message message) {
        this.sessionId = sessionId;
        this.seq = seq;
        this.message = message;
    }

    @Client
    public NewMessageNoticePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public long sessionId() {
        return this.sessionId;
    }

    @Getter
    public long seq() {
        return this.seq;
    }

    @Getter
    public Message message() {
        return this.message;
    }
}
