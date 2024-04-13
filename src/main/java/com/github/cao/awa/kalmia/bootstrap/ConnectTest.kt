package com.github.cao.awa.kalmia.bootstrap;

public class ConnectTest {
    public static void main(String[] args) throws Exception {
        testConnect();
    }

    //    @Test
    public static void testConnect() throws Exception {
        Kalmia.startClient();
    }

    private static void startServer() throws Exception {
        Kalmia.startServer();
    }
}
