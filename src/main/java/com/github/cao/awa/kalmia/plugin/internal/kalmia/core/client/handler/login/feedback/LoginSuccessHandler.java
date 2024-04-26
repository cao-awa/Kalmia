package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.feedback;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginSuccessEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.kalmia.resource.upload.ResourceUpload;
import com.github.cao.awa.kalmia.session.communal.CommunalSession;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class LoginSuccessHandler implements LoginSuccessEventHandler {
    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, LoginSuccessPacket packet) {
        System.out.println("---Login success---");
        System.out.println("UID: " + packet.accessIdentity());
        System.out.println("Token: " + Mathematics.radix(packet.token(),
                                                         36
        ));

        router.accessIdentity(packet.accessIdentity());

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

//        router.send(new RequestGroupSessionPacket("Test group"));

        if (false) {
            router.send(new SelectMessagePacket(CommunalSession.TEST_COMMUNAL_IDENTITY,
                                                0,
                                                114514
            ));
        }

        if (false) {
            router.send(new SendMessagePacket(CommunalSession.TEST_COMMUNAL_IDENTITY,
                                              KalmiaEnv.testKeypairIdentity0,
                                              KalmiaEnv.testKeypairIdentity1,
                                              "Awa".getBytes(StandardCharsets.UTF_8),
                                              new byte[]{1},
                                              false
            ).receipt(Packet.createReceipt()));
        }

        if (false) {
            try {
                byte[] source = "Awa123".getBytes(StandardCharsets.UTF_8);

                byte[] msg = Crypto.ecEncrypt(source,
                                              (ECPublicKey) Kalmia.CLIENT.getPublicKey(KalmiaEnv.testKeypairIdentity0,
                                                                                       true
                                              )
                );
                byte[] sign = Crypto.ecSign(source,
                                            (ECPrivateKey) Kalmia.CLIENT.getPrivateKey(KalmiaEnv.testKeypairIdentity1,
                                                                                       true
                                            )
                );

                router.send(new SendMessagePacket(CommunalSession.TEST_COMMUNAL_IDENTITY,
                                                  KalmiaEnv.testKeypairIdentity0,
                                                  KalmiaEnv.testKeypairIdentity1,
                                                  msg,
                                                  sign,
                                                  false
                ).receipt(Packet.createReceipt()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (false) {
            router.executor()
                  .execute(() -> {
                      ResourceUpload.upload(new File("E:\\Codes\\Java\\Kalmia\\res\\a.jpg"),
                                            router
                      );
                  });
        }

        if (false) {
            try {
                byte[] source = "Test message #{TIME}".getBytes(StandardCharsets.UTF_8);

                byte[] sign = Crypto.ecSign(source,
                                            (ECPrivateKey) Kalmia.CLIENT.getPrivateKey(KalmiaEnv.testKeypairIdentity1,
                                                                                       true
                                            )
                );

                router.send(new SendMessagePacket(CommunalSession.TEST_COMMUNAL_IDENTITY,
                                                  KalmiaConstant.UNMARKED_PURE_IDENTITY,
                                                  KalmiaEnv.testKeypairIdentity1,
                                                  source,
                                                  sign,
                                                  false
                ));
            } catch (Exception e) {

            }
        }

        if (false) {
            try {
                byte[] source = "Test message #{TIME}".getBytes(StandardCharsets.UTF_8);

                router.send(new SendMessagePacket(CommunalSession.TEST_COMMUNAL_IDENTITY,
                                                  KalmiaConstant.UNMARKED_PURE_IDENTITY,
                                                  KalmiaConstant.UNMARKED_PURE_IDENTITY,
                                                  source,
                                                  new byte[]{},
                                                  false
                ).receipt(Packet.createReceipt()));
            } catch (Exception e) {

            }
        }
    }
}

