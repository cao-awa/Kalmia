package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.SessionListenersUpdateEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(id = 500, crypto = true)
@NetworkEventTarget(SessionListenersUpdateEvent.class)
public class SessionListenersUpdatePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private List<Long> listeners;

    @Server
    public SessionListenersUpdatePacket(List<Long> listeners) {
        this.listeners = listeners;
    }

    @Auto
    @Client
    public SessionListenersUpdatePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public List<Long> listeners() {
        return this.listeners;
    }
}
