package com.github.cao.awa.kalmia.event.handler.network.inbound.disconnect;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.network.inbound.disconnect.TryDisconnectEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.disconnet.TryDisconnectPacket;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoHandler(TryDisconnectEvent.class)
public interface TryDisconnectEventHandler extends NetworkEventHandler<TryDisconnectPacket, TryDisconnectEvent> {
}
