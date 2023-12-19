package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.invalid.operation;

import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface DeleteMessageEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, PureExtraIdentity sessionIdentity, long seq);
}
