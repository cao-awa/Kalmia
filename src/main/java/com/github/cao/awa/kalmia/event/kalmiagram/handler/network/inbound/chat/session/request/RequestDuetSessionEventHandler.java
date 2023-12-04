package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.request;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.request.RequestDuetSessionEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(RequestDuetSessionEvent.class)
public interface RequestDuetSessionEventHandler extends NetworkEventHandler<RequestDuetSessionPacket, RequestDuetSessionEvent> {

}

