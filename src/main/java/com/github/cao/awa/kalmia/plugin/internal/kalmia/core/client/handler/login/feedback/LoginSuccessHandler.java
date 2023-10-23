package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.feedback;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginSuccessEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class LoginSuccessHandler implements LoginSuccessEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, LoginSuccessPacket packet) {
        System.out.println("---Login success---");
        System.out.println("UID: " + packet.uid());
        System.out.println("Token: " + Mathematics.radix(packet.token(),
                                                         36
        ));

        router.setUid(packet.uid());

        router.setStates(RequestState.AUTHED);

//        EntrustEnvironment.thread(() -> {
//                              for (int i = 0; i < 500; i++) {
//                                  router.send(new SendMessageRequest(123,
//                                                                     BytesRandomIdentifier.create(16),
//                                                                     new PlainMessage("fuck cao_awa " + i + " times",
//                                                                                      this.uid
//                                                                     ).toBytes()
//                                  ));
//                              }
//                          })
//                          .start();

//        TrafficCount.show();

//        router.send(new SelectMessagePacket(123456,
//                                            0,
//                                            200
//        ));

//        router.send(new RequestDuetSessionPacket(2));

//        router.send(new SendMessagePacket(
//                0,
//                new PlainsMessage("test123",
//                                  packet.uid()
//                ),
//                Packet.createReceipt()
//        ));

//        for (int i = 0; i < 100; i++) {
//            router.send(new SendMessagePacket(0,
//                                              new PlainsMessage(" awa: " + i,
//                                                                packet.uid()
//                                              ),
//                                              Packet.createReceipt()
//            ));
//        }

        router.send(new SelectMessagePacket(0,
                                            0,
                                            114514
        ));

//        // TODO Test only
//        router.send(new DeleteMessageRequest(123,
//                                             2
//        ));
//        router.send(new DisableInstanceRequest());
    }
}
