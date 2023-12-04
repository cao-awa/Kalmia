package com.github.cao.awa.kalmia.network.packet.inbound.disconnet;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.disconnect.TryDisconnectEvent;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;

@AutoSolvedPacket(id = 114514, crypto = false)
@NetworkEventTarget(TryDisconnectEvent.class)
public class TryDisconnectPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private String reason;

    public TryDisconnectPacket(String reason) {
        this.reason = reason;
    }

    @Auto
    public TryDisconnectPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public String reason() {
        return this.reason;
    }
}
