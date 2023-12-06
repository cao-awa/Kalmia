package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.pubkey.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.pubkey.select.SelectPublicKeyEventHandler;
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select.SelectPublicKeyPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.pubkey.select.SelectedPublicKeyPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.security.PublicKey;
import java.util.Map;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class SelectPublicKeyHandler implements SelectPublicKeyEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, SelectPublicKeyPacket packet) {
        Map<Long, KeyPairStore> result = ApricotCollectionFactor.hashMap();

        packet.ids()
              .forEach(id -> {
                  PublicKey publicKey = Kalmia.SERVER.keypairManager()
                                                     .publicKey(id);
                  byte[] privateKey = BytesUtil.EMPTY;
                  if (Kalmia.SERVER.userManager()
                                   .keyStores(router.uid())
                                   .contains(id)) {
                      privateKey = Kalmia.SERVER.keypairManager()
                                                .privateKey(id);
                  }

                  result.put(id,
                             KeyStoreIdentity.createKeyPairStore(publicKey,
                                                                 privateKey
                             )
                  );
              });

        if (result.size() > 0) {
            router.send(new SelectedPublicKeyPacket(result).receipt(packet.receipt()));
        }
    }
}
