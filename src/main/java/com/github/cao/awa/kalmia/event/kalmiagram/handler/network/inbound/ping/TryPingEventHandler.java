package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.ping;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.ping.TryPingEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingPacket;

@Auto
@AutoHandler(TryPingEvent.class)
public interface TryPingEventHandler extends NetworkEventHandler<TryPingPacket, TryPingEvent> {
}
