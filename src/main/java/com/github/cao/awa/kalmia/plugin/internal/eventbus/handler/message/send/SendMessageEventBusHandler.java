package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send;

import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface SendMessageEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, PureExtraIdentity sessionIdentity, PureExtraIdentity keyIdentity, PureExtraIdentity signIdentity, byte[] message, byte[] sign);
}
