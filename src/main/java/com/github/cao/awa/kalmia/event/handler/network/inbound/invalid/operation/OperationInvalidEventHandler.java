package com.github.cao.awa.kalmia.event.handler.network.inbound.invalid.operation;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.invalid.operation.OperationInvalidEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(OperationInvalidEvent.class)
public interface OperationInvalidEventHandler extends NetworkEventHandler<OperationInvalidPacket, OperationInvalidEvent> {
}
