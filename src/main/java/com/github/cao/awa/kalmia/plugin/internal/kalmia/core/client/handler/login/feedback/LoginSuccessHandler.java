package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.login.feedback;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.feedback.LoginSuccessEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.message.crypt.AsymmetricCryptedMessage;
import com.github.cao.awa.kalmia.message.plains.PlainsMessage;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.login.feedback.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState;
import com.github.cao.awa.kalmia.resource.upload.ResourceUpload;
import com.github.cao.awa.modmdo.annotation.platform.Client;

import java.io.File;
import java.nio.charset.StandardCharsets;
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
        System.out.println("UID: " + packet.uid());
        System.out.println("Token: " + Mathematics.radix(packet.token(),
                                                         36
        ));

        router.uid(packet.uid());

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
            for (int i = 0; i < 50; i++) {
                router.send(new SendMessagePacket(0,
                                                  new PlainsMessage(" awa: " + i,
                                                                    packet.uid()
                                                  )
                ).receipt(Packet.createReceipt()));
            }
        }

        if (false) {
            router.send(new SendMessagePacket(0,
                                              new AsymmetricCryptedMessage(0,
                                                                           1,
                                                                           "Awa".getBytes(StandardCharsets.UTF_8),
                                                                           new byte[]{1},
                                                                           packet.uid()
                                              )
            ).receipt(Packet.createReceipt()));
        }

        if (false) {
            try {
                byte[] msg = Crypto.ecEncrypt("Awa123".getBytes(StandardCharsets.UTF_8),
                                              (ECPublicKey) KalmiaEnv.testKeypair0.publicKey()
                                                                                  .decode()
                );
                byte[] sign = Crypto.ecSign("Awa123".getBytes(StandardCharsets.UTF_8),
                                            Crypto.decodeEcPrikey(Crypto.aesDecrypt(KalmiaEnv.testKeypair1.privateKey()
                                                                                                          .key(),
                                                                                    KalmiaEnv.testUer2AesCipher
                                            ))
                );

                router.send(new SendMessagePacket(0,
                                                  new AsymmetricCryptedMessage(0,
                                                                               1,
                                                                               msg,
                                                                               sign,
                                                                               packet.uid()
                                                  )
                ).receipt(Packet.createReceipt()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        KalmiaEnv.awaitManager.notice(packet.receipt());

        if (false) {
            router.executor()
                  .execute(() -> {
                      ResourceUpload.upload(new File("E:\\Codes\\Java\\Kalmia\\res\\a.jpg"),
                                            router
                      );
                  });
        }

//        // TODO Test only
//        router.send(new DeleteMessageRequest(123,
//                                             2
//        ));
//        router.send(new DisableInstanceRequest());
    }
}
