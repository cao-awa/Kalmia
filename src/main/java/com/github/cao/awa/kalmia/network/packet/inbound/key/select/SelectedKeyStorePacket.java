package com.github.cao.awa.kalmia.network.packet.inbound.key.select;

import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.key.select.SelectedKeyStoreEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 151, crypto = true)
@NetworkEventTarget(SelectedKeyStoreEvent.class)
public class SelectedKeyStorePacket extends Packet<AuthedRequestHandler> {
    private Map<PureExtraIdentity, KeyPairStore> keys;
}
