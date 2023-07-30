package com.github.cao.awa.kalmia.event.handler.network.inbound.login.token;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.login.token.LoginWithTokenEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.token.LoginWithTokenPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(LoginWithTokenEvent.class)
public interface LoginWithTokenEventHandler extends NetworkEventHandler<LoginWithTokenPacket, LoginWithTokenEvent> {
}
