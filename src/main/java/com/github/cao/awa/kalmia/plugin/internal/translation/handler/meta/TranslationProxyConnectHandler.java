package com.github.cao.awa.kalmia.plugin.internal.translation.handler.meta;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.translation.event.handler.inbound.meta.connect.TranslationProxyConnectEventHandler;
import com.github.cao.awa.kalmia.translation.network.packet.meta.connect.TranslationProxyConnectPacket;
import com.github.cao.awa.kalmia.translation.network.packet.meta.status.TranslationProxyStatusPacket;
import com.github.cao.awa.kalmia.translation.network.router.TranslationRouter;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationProxyConnectHandler implements TranslationProxyConnectEventHandler {
    @Auto
    @Override
    public void handle(TranslationRouter router, TranslationProxyConnectPacket packet) {
        router.clientIdentity(packet.clientIdentity());
        router.shouldSaveData(packet.dataSave());

        router.send(new TranslationProxyStatusPacket("status.kalmia.connecting"));

        RequestRouter oldRouter = KalmiaTranslationEnv.router(router);

        if (oldRouter != null) {
            oldRouter.disconnect();
        }

        Kalmia.SERVER.task(() -> {
//            try {
//                if (Kalmia.CLIENT == null) {
//                    Kalmia.CLIENT = new KalmiaClient();
//                }
//
//                router.funeral(Kalmia.CLIENT :: disconnect);
//
//                Kalmia.CLIENT.activeCallback(r -> {
//                    KalmiaTranslationEnv.router(router,
//                                                r
//                    );
//
//                    r.funeral(() -> {
//                        KalmiaTranslationEnv.clearIdentity(r);
//                    });
//
//                    KalmiaTranslationEnv.identity(r,
//                                                  router
//                    );
////            router.send(new ClientHelloRequest(KalmiaEnv.STANDARD_REQUEST_PROTOCOL,
////                                               "KalmiaWww v1.0.0"
////            ));
//
//                    r.send(new ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
//                                                 "KalmiaWww v1.0.1"
//                    ));
//
////                            System.out.println("W: " + Mathematics.radix(test,
////                                                                                         36
////                            ));
//                });
//
////                new KalmiaClientNetworkIo(Kalmia.CLIENT).connect()
////                );
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        });
    }
}
