package com.github.cao.awa.kalmia.plugin.internal.translation.handler.meta;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;
import com.github.cao.awa.kalmia.translation.event.handler.inbound.meta.connect.TranslationProxyConnectEventHandler;
import com.github.cao.awa.kalmia.translation.network.packet.meta.connect.TranslationProxyConnectPacket;
import com.github.cao.awa.kalmia.translation.network.packet.meta.status.TranslationProxyStatusPacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class TranslationProxyConnectHandler implements TranslationProxyConnectEventHandler {
    @Auto
    @Override
    public void handle(TranslationRouter router, TranslationProxyConnectPacket packet) {
        router.clientIdentity(packet.clientIdentity());

        router.send(new TranslationProxyStatusPacket("status.kalmia.connecting"));

        RequestRouter oldRouter = KalmiaTranslationEnv.router(router);

        if (oldRouter != null) {
            oldRouter.disconnect();
        }

        Kalmia.SERVER.task(() -> {
            try {
                KalmiaClient client = new KalmiaClient();

                router.funeral(client :: disconnect);

                client.activeCallback(r -> {
                    KalmiaTranslationEnv.router(router,
                                                r
                    );

                    r.funeral(() -> {
                        KalmiaTranslationEnv.clearIdentity(r);
                    });

                    KalmiaTranslationEnv.identity(r,
                                                  router
                    );
//            router.send(new ClientHelloRequest(KalmiaEnv.STANDARD_REQUEST_PROTOCOL,
//                                               "KalmiaWww v1.0.0"
//            ));

                    r.send(new ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
                                                 "KalmiaWww v1.0.1"
                    ));

//                            System.out.println("W: " + Mathematics.radix(test,
//                                                                                         36
//                            ));
                });

                new KalmiaClientNetworkIo(client).connect(packet.host(),
                                                          packet.port()
                );
            } catch (Exception e) {

            }
        });
    }
}
