package com.github.cao.awa.kalmia.event.handler.network.inbound.login.sign;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.login.sign.LoginWithSignEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(LoginWithSignEvent.class)
public interface LoginWithSignEventHandler extends NetworkEventHandler<LoginWithSignPacket, LoginWithSignEvent> {
}
