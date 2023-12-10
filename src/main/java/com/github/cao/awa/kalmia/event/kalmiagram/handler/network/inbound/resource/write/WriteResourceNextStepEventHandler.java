package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.resource.write;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.resource.write.WriteResourceNextStepEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.resource.write.WriteResourceNextStepPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(WriteResourceNextStepEvent.class)
public interface WriteResourceNextStepEventHandler extends NetworkEventHandler<WriteResourceNextStepPacket, WriteResourceNextStepEvent> {
}
