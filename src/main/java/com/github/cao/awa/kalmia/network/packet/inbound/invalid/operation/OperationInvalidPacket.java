package com.github.cao.awa.kalmia.network.packet.inbound.invalid.operation;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.actor.Getter;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotation.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.network.inbound.invalid.operation.OperationInvalidEvent;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.nio.charset.StandardCharsets;

@AutoSolvedPacket(id = 2147483647)
@NetworkEventTarget(OperationInvalidEvent.class)
public class OperationInvalidPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private final String reason;

    @Server
    public OperationInvalidPacket(String reason) {
        this.reason = reason;
    }

    @Auto
    @Client
    public OperationInvalidPacket(BytesReader reader) {
        this(new String(reader.all(),
                        StandardCharsets.UTF_8
        ));
    }

    @Getter
    public String reason() {
        return this.reason;
    }
}
