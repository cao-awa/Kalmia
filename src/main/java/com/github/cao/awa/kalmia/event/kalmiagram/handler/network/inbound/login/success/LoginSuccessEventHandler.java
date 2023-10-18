package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.success;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.success.LoginSuccessEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(LoginSuccessEvent.class)
public interface LoginSuccessEventHandler extends NetworkEventHandler<LoginSuccessPacket, LoginSuccessEvent> {
}
