package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket;

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

        KalmiaEnv.setupClient();

        UnsolvedPacketFactor.register();

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
    }

    private static void startServer() throws Exception {
        Kalmia.startServer();
    }
}
