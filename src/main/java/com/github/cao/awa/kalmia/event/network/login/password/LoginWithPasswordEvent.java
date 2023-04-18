package com.github.cao.awa.kalmia.event.network.login.password;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.handler.login.LoginHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.TriConsumer;

import java.util.Set;

@Server
public class LoginWithPasswordEvent {
    private static final Set<TriConsumer<LoginWithPasswordPacket, UnsolvedRequestRouter, LoginHandler>> actions = ApricotCollectionFactor.newHashSet();

    public static void subscribe(TriConsumer<LoginWithPasswordPacket, UnsolvedRequestRouter, LoginHandler> action) {
        actions.add(action);
    }

    public static void trigger(LoginWithPasswordPacket packet, UnsolvedRequestRouter router, LoginHandler handler) {
        for (TriConsumer<LoginWithPasswordPacket, UnsolvedRequestRouter, LoginHandler> action : actions) {
            action.accept(packet,
                          router,
                          handler
            );
        }
    }
}
