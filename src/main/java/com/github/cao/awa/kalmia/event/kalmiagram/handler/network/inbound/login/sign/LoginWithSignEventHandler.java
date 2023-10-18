package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.sign;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.LoginEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.sign.LoginWithSignEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(LoginWithSignEvent.class)
public interface LoginWithSignEventHandler extends LoginEventHandler<LoginWithSignPacket, LoginWithSignEvent> {
}
