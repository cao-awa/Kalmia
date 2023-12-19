package com.github.cao.awa.kalmia.network.packet.inbound.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select.SelectedKeyStoreEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Map;

@AutoSolvedPacket(id = 151, crypto = true)
@NetworkEventTarget(SelectedKeyStoreEvent.class)
public class SelectedKeyStorePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private Map<PureExtraIdentity, KeyPairStore> keys;

    @Server
    public SelectedKeyStorePacket(Map<PureExtraIdentity, KeyPairStore> keys) {
        this.keys = keys;
    }

    @Server
    public SelectedKeyStorePacket(PureExtraIdentity id, KeyPairStore key) {
        this.keys = ApricotCollectionFactor.hashMap();
        this.keys.put(id,
                      key
        );
    }

    @Auto
    @Client
    public SelectedKeyStorePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public Map<PureExtraIdentity, KeyPairStore> keys() {
        return this.keys;
    }
}
