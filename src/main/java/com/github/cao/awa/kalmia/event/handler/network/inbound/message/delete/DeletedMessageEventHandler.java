package com.github.cao.awa.kalmia.event.handler.network.inbound.message.delete;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.message.delete.DeletedMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(DeletedMessageEvent.class)
public interface DeletedMessageEventHandler extends NetworkEventHandler<DeletedMessagePacket, DeletedMessageEvent> {
}
