package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.resource.write.RequestNextResourceShardEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.RequestNextResourceShardPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(RequestNextResourceShardEvent.class)
public interface RequestNextResourceShardEventHandler extends NetworkEventHandler<RequestNextResourceShardPacket, RequestNextResourceShardEvent> {
}
