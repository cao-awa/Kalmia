package com.github.cao.awa.kalmia.network.packet.inbound.disconnet;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.disconnect.TryDisconnectEvent;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;

@AutoSolvedPacket(id = 114514)
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
