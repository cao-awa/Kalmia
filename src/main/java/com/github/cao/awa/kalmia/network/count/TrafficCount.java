package com.github.cao.awa.kalmia.network.count;

public class TrafficCount {
    private static long encoded = 0;
    private static long decoded = 0;
    private static long sent = 0;
    private static long received = 0;

    public static void encoded(int length) {
        encoded += length;
    }

    public static void decoded(int length) {
        decoded += length;
    }

    public static void sent(int length) {
        sent += length;
    }

    public static void received(int length) {
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
