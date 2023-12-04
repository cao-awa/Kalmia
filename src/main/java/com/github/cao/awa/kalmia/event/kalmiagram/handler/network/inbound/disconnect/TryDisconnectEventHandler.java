package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.disconnect;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.disconnect.TryDisconnectEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;

@Auto
@AutoHandler(TryDisconnectEvent.class)
public interface TryDisconnectEventHandler extends NetworkEventHandler<TryDisconnectPacket, TryDisconnectEvent> {
}
