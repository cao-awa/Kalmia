package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.pubkey.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.pubkey.select.SelectedPublicKeyEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select.SelectedPublicKeyPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class SelectedPublicKeyHandler implements SelectedPublicKeyEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, SelectedPublicKeyPacket packet) {
        packet.keys()
              .forEach((id, key) -> {
                  System.out.println("Selected public key: " + id + " " + key.type());

                  Kalmia.CLIENT.keypairManager()
                               .publicKey(id,
                                          key.publicKey()
                                             .decode()
                               );
              });

        KalmiaEnv.awaitManager.notice(packet.receipt());
    }
}
