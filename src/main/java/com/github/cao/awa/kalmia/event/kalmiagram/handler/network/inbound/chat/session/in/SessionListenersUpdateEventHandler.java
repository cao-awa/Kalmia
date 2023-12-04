package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.in;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.SessionListenersUpdateEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.listeners.SessionListenersUpdatePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SessionListenersUpdateEvent.class)
public interface SessionListenersUpdateEventHandler extends NetworkEventHandler<SessionListenersUpdatePacket, SessionListenersUpdateEvent> {

}

