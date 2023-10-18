package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.failed;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.failed.LoginFailedEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.login.failed.LoginFailedPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(LoginFailedEvent.class)
public interface LoginFailedEventHandler extends NetworkEventHandler<LoginFailedPacket, LoginFailedEvent> {
}
