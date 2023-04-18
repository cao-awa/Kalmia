package com.github.cao.awa.kalmia.event.network.login.success;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.inbound.SolvedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.TriConsumer;

import java.util.Set;

@Client
public class LoginSuccessEvent {
    private static final Set<TriConsumer<LoginSuccessPacket, UnsolvedRequestRouter, SolvedRequestHandler>> actions = ApricotCollectionFactor.newHashSet();

    public static void subscribe(TriConsumer<LoginSuccessPacket, UnsolvedRequestRouter, SolvedRequestHandler> action) {
        actions.add(action);
    }

    public static void trigger(LoginSuccessPacket packet, UnsolvedRequestRouter router, SolvedRequestHandler handler) {
        for (TriConsumer<LoginSuccessPacket, UnsolvedRequestRouter, SolvedRequestHandler> action : actions) {
            action.accept(packet,
                          router,
                          handler
            );
        }
    }
}
