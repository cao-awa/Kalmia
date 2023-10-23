package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;

public class ConnectTest {
    public static void main(String[] args) throws Exception {
        new ConnectTest().testConnect();
    }

    //    @Test
    public void testConnect() throws Exception {
//        EntrustEnvironment.thread(() -> {
//                              try {
//                                  startServer();
//                              } catch (Exception e) {
//                                  throw new RuntimeException(e);
//                              }
//                          })
//                          .start();
//
//        while (Kalmia.SERVER == null || ! Kalmia.SERVER.isStarted()) {
//            Thread.sleep(10);
//        }

        Kalmia.setupBootstrapConfig();

        KalmiaEnv.setupClient();

        UnsolvedPacketFactor.register();

        KalmiaClient client = new KalmiaClient();

        client.activeCallback(router -> {
//            router.send(new ClientHelloRequest(KalmiaEnv.STANDARD_REQUEST_PROTOCOL,
//                                               "KalmiaWww v1.0.0"
//            ));

//            router.send(new ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
//                                              "KalmiaWww v1.0.1"
//            ));

            router.send(new LoginWithPasswordPacket(1,
                                                    "123456"
            ));

//            router.send(new SelectMessagePacket(1,
//                                                0,
//                                                100
//            ));

//                            System.out.println("W: " + Mathematics.radix(test,
//                                                                                         36
//                            ));
        });

        new KalmiaClientNetworkIo(client).connect("127.0.0.1",
                                                  12345
        );
    }

    private static void startServer() throws Exception {
        Kalmia.startServer();
    }
}
