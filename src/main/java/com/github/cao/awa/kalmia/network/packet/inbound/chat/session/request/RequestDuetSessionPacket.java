package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.request.RequestDuetSessionEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * This packet sender is client, use to request the session id for a target. <br>
 * The response of this packet is {@link ChatInSessionPacket}.
 *
 * @author cao_awa
 * @see ChatInSessionPacket
 * @since 1.0.0
 */
@AutoSolvedPacket(id = 50, crypto = true)
@NetworkEventTarget(RequestDuetSessionEvent.class)
public class RequestDuetSessionPacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private long targetUid;

    @Client
    public RequestDuetSessionPacket(long targetUid) {
        this.targetUid = targetUid;
    }

    @Auto
    @Server
    public RequestDuetSessionPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public long targetUid() {
        return this.targetUid;
    }
}
