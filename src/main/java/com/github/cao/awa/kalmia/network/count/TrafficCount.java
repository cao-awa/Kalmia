package com.github.cao.awa.kalmia.network.count;

public class TrafficCount {
    private static long encoded = 0;
    private static long decoded = 0;
    private static long sent = 0;
    private static long received = 0;

    public static void encode(int length) {
        encoded += length;
    }

    public static void decode(int length) {
        decoded += length;
    }

    public static void send(int length) {
        sent += length;
    }

    public static void receive(int length) {
        received += length;
    }

    public static void show() {
        System.out.println("----Traffic");
        System.out.println("Sent: " + sent + " (bytes)");
        System.out.println("Encoded: " + encoded + " (bytes)");
        System.out.println("Received: " + received + " (bytes)");
        System.out.println("Decoded: " + decoded + " (bytes)");
    }
}
