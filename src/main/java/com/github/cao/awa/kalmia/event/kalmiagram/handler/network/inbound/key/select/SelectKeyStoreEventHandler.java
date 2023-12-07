package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select.SelectKeyStoreEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(SelectKeyStoreEvent.class)
public interface SelectKeyStoreEventHandler extends NetworkEventHandler<SelectKeyStorePacket, SelectKeyStoreEvent> {
}
