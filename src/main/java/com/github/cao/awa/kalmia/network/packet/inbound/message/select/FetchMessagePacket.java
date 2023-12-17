package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.FetchMessageEvent;
import com.github.cao.awa.kalmia.identity.MillsAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(id = 17, crypto = true)
@NetworkEventTarget(FetchMessageEvent.class)
public class FetchMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private List<MillsAndExtraIdentity> identities;

    @Client
    public FetchMessagePacket(List<MillsAndExtraIdentity> identities) {
        this.identities = identities;
    }

    @Auto
    @Server
    public FetchMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public List<MillsAndExtraIdentity> identities() {
        return this.identities;
    }
}
