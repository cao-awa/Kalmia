package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners;

import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.SessionListenersUpdateEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 500, crypto = true)
@NetworkEventTarget(SessionListenersUpdateEvent.class)
public class SessionListenersUpdatePacket extends Packet<AuthedRequestHandler> {
    private List<Session> sessions;

    public Set<PureExtraIdentity> mapId() {
        return sessions().stream()
                         .map(Session :: identity)
                         .collect(Collectors.toSet());
    }
}
