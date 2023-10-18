package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.chat.session.in;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.ChatInSessionEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(ChatInSessionEvent.class)
public interface ChatInSessionEventHandler extends NetworkEventHandler<ChatInSessionPacket, ChatInSessionEvent> {

}

