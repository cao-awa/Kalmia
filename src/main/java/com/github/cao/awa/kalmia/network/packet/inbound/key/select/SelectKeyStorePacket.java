package com.github.cao.awa.kalmia.network.packet.inbound.key.select;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select.SelectKeyStoreEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 150, crypto = true)
@NetworkEventTarget(SelectKeyStoreEvent.class)
public class SelectKeyStorePacket extends Packet<AuthedRequestHandler> {
    private List<PureExtraIdentity> keyIdentities;

    @Client
    public SelectKeyStorePacket(PureExtraIdentity id) {
        this.keyIdentities = ApricotCollectionFactor.arrayList();
        this.keyIdentities.add(id);
    }
}
