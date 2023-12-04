package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.test;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.test.RequestTestEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.test.RequestTestPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(RequestTestEvent.class)
public interface RequestTestEventHandler extends NetworkEventHandler<RequestTestPacket, RequestTestEvent> {

}

