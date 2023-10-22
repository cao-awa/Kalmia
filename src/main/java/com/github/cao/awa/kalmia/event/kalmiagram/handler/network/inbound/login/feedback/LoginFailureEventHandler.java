package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.feedback.LoginFailureEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginFailurePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(LoginFailureEvent.class)
public interface LoginFailureEventHandler extends NetworkEventHandler<LoginFailurePacket, LoginFailureEvent> {
}
