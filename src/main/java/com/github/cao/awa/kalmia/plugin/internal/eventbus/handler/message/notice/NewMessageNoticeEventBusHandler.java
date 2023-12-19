package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.notice;

import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.EventBusHandler;

public interface NewMessageNoticeEventBusHandler extends EventBusHandler {
    void handle(RequestRouter router, byte[] receipt, PureExtraIdentity sessionIdentity, long seq, Message message);
}
