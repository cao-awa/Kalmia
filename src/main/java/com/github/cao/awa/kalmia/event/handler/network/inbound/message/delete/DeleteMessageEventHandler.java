package com.github.cao.awa.kalmia.event.handler.network.inbound.message.delete;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.message.delete.DeleteMessageEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeleteMessagePacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(DeleteMessageEvent.class)
public interface DeleteMessageEventHandler extends NetworkEventHandler<DeleteMessagePacket, DeleteMessageEvent> {
}
