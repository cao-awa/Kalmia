package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.pubkey.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.pubkey.select.SelectedPublicKeyEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select.SelectedPublicKeyPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(SelectedPublicKeyEvent.class)
public interface SelectedPublicKeyEventHandler extends NetworkEventHandler<SelectedPublicKeyPacket, SelectedPublicKeyEvent> {
}
