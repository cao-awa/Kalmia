package com.github.cao.awa.kalmia.network.packet.inbound.test;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.test.RequestTestEvent;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(id = Integer.MAX_VALUE - 1, crypto = true)
@NetworkEventTarget(RequestTestEvent.class)
public class RequestTestPacket extends Packet<StatelessHandler> {
    @AutoData
    private String clientVersion;

    @Client
    public RequestTestPacket(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    @Auto
    @Server
    public RequestTestPacket(BytesReader reader) {
        super(reader);
    }

    public String clientVersion() {
        return this.clientVersion;
    }
}
