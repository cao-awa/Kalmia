package com.github.cao.awa.kalmia.event.handler.network.inbound.login.password;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.inbound.login.LoginEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(LoginWithPasswordEvent.class)
public interface LoginWithPasswordEventHandler extends LoginEventHandler<LoginWithPasswordPacket, LoginWithPasswordEvent> {
}
