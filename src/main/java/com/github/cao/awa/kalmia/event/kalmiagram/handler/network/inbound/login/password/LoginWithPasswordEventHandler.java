package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.password;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.LoginEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(LoginWithPasswordEvent.class)
public interface LoginWithPasswordEventHandler extends LoginEventHandler<LoginWithPasswordPacket, LoginWithPasswordEvent> {
}
