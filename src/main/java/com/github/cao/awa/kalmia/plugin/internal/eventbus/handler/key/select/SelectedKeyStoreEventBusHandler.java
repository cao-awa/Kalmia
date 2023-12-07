package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.key.select;

import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

import java.util.Map;

public interface SelectedKeyStoreEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, Map<Long, KeyPairStore> keys);
}
