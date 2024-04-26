package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.SelectMessageEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 12, crypto = true)
@NetworkEventTarget(SelectMessageEvent.class)
public class SelectMessagePacket extends Packet<AuthedRequestHandler> {
    private PureExtraIdentity sessionIdentity;
    private long from;
    private long to;
}
