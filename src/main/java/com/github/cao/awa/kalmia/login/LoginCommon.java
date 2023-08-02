package com.github.cao.awa.kalmia.login;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class LoginCommon {
    public static void login(long uid, RequestRouter router) {
        Kalmia.SERVER.login(uid,
                            router
        );

        router.setUid(uid);
    }

    public static void logout(long uid, RequestRouter router) {
        Kalmia.SERVER.logout(uid,
                             router
        );
    }
}
