package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.ping;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.ping.TryPingResponseEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;

@Auto
@AutoHandler(TryPingResponseEvent.class)
public interface TryPingResponseEventHandler extends NetworkEventHandler<TryPingResponsePacket, TryPingResponseEvent> {
}
