import com.github.cao.awa.kalmia.client.KalmiaClient;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.kalmia.network.packet.inbound.test.TestPacket;

public class ConnectionBenchmark {
    public static void main(String[] args) throws Exception {
        new ConnectionBenchmark().testConnect();
    }

    //    @Test
    public void testConnect() throws Exception {
        KalmiaEnv.setupClient();

        UnsolvedPacketFactor.register();

        for (int i = 0; i < 10000; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    KalmiaClient client = new KalmiaClient();

                    client.activeCallback(router -> {
//            router.send(new ClientHelloRequest(KalmiaEnv.STANDARD_REQUEST_PROTOCOL,
//                                               "KalmiaWww v1.0.0"
//            ));

                        router.send(new TestPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL,
                                                   "KalmiaWww v1.0.1 / " + finalI
                        ));

//                            System.out.println("W: " + Mathematics.radix(test,
//                                                                                         36
//                            ));
                    });

                    new KalmiaClientNetworkIo(client).connect("127.0.0.1",
                                                              12345
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();

            Thread.sleep(100);
        }
    }
}
