package com.github.cao.awa.kalmia.plugin.internal.translation.handler.done.launch;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.event.handler.launch.done.DoneLaunchEventHandler;
import com.github.cao.awa.kalmia.event.launch.done.DoneLaunchEvent;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;

@Auto
@PluginRegister(name = "kalmia_translation")
public class DoneLaunchHandler implements DoneLaunchEventHandler {
    @Override
    public void handle(DoneLaunchEvent event) {
        System.out.println("Done launch");

        try {
            KalmiaClient client = new KalmiaClient();

            client.activeCallback(router -> {
//            router.send(new ClientHelloRequest(KalmiaEnv.STANDARD_REQUEST_PROTOCOL,
//                                               "KalmiaWww v1.0.0"
//            ));

                router.send(new ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
                                                  "KalmiaWww v1.0.1"
                ));

//                            System.out.println("W: " + Mathematics.radix(test,
//                                                                                         36
//                            ));
            });

            new KalmiaClientNetworkIo(client).connect("127.0.0.1",
                                                      12345
            );
        } catch (Exception e) {

        }
    }
}
