package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.ChatInSessionEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

/**
 * This packet sender is server, response client request the session id for a target.
 * A sending case is in {@link RequestDuetSessionPacket}.
 *
 * @author cao_awa
 * @see RequestDuetSessionPacket
 * @since 1.0.0
 */
@AutoSolvedPacket(id = 16, crypto = true)
@NetworkEventTarget(ChatInSessionEvent.class)
public class ChatInSessionPacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private LongAndExtraIdentity targetUser;
    @AutoData
    @DoNotSet
    private PureExtraIdentity sessionIdentity;

    @Server
    public ChatInSessionPacket(LongAndExtraIdentity targetUser, PureExtraIdentity sessionIdentity) {
        this.targetUser = targetUser;
        this.sessionIdentity = sessionIdentity;
    }

    @Auto
    @Client
    public ChatInSessionPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public LongAndExtraIdentity targetUser() {
        return this.targetUser;
    }

    @Getter
    public PureExtraIdentity sessionIdentity() {
        return this.sessionIdentity;
    }
}
