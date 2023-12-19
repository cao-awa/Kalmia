package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.SessionListenersUpdateEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.session.Session;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(id = 500, crypto = true)
@NetworkEventTarget(SessionListenersUpdateEvent.class)
public class SessionListenersUpdatePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private List<Session> sessions;

    @Server
    public SessionListenersUpdatePacket(List<Session> sessions) {
        this.sessions = sessions;
    }

    @Auto
    @Client
    public SessionListenersUpdatePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public List<Session> sessions() {
        return this.sessions;
    }

    public List<PureExtraIdentity> mapId() {
        return sessions().stream()
                         .map(Session :: identity).
                         toList();
    }
}
