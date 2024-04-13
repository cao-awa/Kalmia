package com.github.cao.awa.kalmia.login;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;

public class LoginCommon {
    public static void login(LongAndExtraIdentity accessIdentity, RequestRouter router) {
        Kalmia.SERVER.login(accessIdentity,
                            router
        );

        router.accessIdentity(accessIdentity);
    }

    public static void logout(LongAndExtraIdentity accessIdentity, RequestRouter router) {
        Kalmia.SERVER.logout(accessIdentity,
                             router
        );
    }
}
