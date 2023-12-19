package com.github.cao.awa.kalmia.network.packet.inbound.message.notice;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.notice.NewMessageNoticeEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = 200000, crypto = true)
@NetworkEventTarget(NewMessageNoticeEvent.class)
public class NewMessageNoticePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private PureExtraIdentity sessionIdentity;
    @AutoData
    @DoNotSet
    private long seq;
    @AutoData
    @DoNotSet
    private Message message;

    @Server
    public NewMessageNoticePacket(PureExtraIdentity sessionIdentity, long seq, Message message) {
        this.sessionIdentity = sessionIdentity;
        this.seq = seq;
        this.message = message;
    }

    @Client
    public NewMessageNoticePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public PureExtraIdentity sessionIdentity() {
        return this.sessionIdentity;
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
