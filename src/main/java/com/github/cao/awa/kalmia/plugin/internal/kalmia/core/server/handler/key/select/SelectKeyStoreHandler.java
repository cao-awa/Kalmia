package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select.SelectKeyStoreEventHandler;
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectedKeyStorePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.Map;
import java.util.Set;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class SelectKeyStoreHandler implements SelectKeyStoreEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, SelectKeyStorePacket packet) {
        Map<Long, KeyPairStore> result = ApricotCollectionFactor.hashMap();

        Set<Long> userStores = Kalmia.SERVER.userManager()
                                            .keyStores(router.uid());

        packet.ids()
              .forEach(id -> {
                  KeyPairStore store = Kalmia.SERVER.keypairManager()
                                                    .getStore(id);

                  // Do not provide private key if the user are not key owner.
                  if (! userStores.contains(id)) {
                      // Clear private key.
                      store = KeyStoreIdentity.createKeyPairStore(store.publicKey()
                                                                       .decode(),
                                                                  BytesUtil.EMPTY
                      );
                  }

                  result.put(id,
                             store
                  );
              });

        router.send(new SelectedKeyStorePacket(result).receipt(packet.receipt()));
    }
}
