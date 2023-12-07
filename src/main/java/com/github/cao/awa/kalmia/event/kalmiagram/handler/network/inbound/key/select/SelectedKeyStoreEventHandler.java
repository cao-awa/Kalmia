package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select.SelectedKeyStoreEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectedKeyStorePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SelectedKeyStoreEvent.class)
public interface SelectedKeyStoreEventHandler extends NetworkEventHandler<SelectedKeyStorePacket, SelectedKeyStoreEvent> {
}
