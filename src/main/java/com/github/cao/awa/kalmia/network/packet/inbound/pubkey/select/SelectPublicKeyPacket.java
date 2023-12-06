package com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.pubkey.select.SelectPublicKeyEvent;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;

@AutoSolvedPacket(id = 150, crypto = true)
@NetworkEventTarget(SelectPublicKeyEvent.class)
public class SelectPublicKeyPacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private List<Long> ids;

    @Client
    public SelectPublicKeyPacket(List<Long> ids) {
        this.ids = ids;
    }

    @Client
    public SelectPublicKeyPacket(Long id) {
        this.ids = ApricotCollectionFactor.arrayList();
        this.ids.add(id);
    }

    @Auto
    @Server
    public SelectPublicKeyPacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public List<Long> ids() {
        return this.ids;
    }
}
