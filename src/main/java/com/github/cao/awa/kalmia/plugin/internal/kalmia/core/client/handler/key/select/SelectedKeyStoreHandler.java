package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.key.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.key.select.SelectedKeyStoreEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectedKeyStorePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class SelectedKeyStoreHandler implements SelectedKeyStoreEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedKeyStorePacket packet) {
        packet.keys()
              .forEach((id, store) -> {
                  System.out.println("Selected public key: " + id + " " + store.type());

                  Kalmia.CLIENT.keypairManager()
                               .set(id,
                                    store
                               );
              });
    }
}
