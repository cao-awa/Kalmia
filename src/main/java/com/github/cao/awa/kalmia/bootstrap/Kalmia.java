package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.kalmia.KalmiaServer;

public class Kalmia {
    public static final KalmiaServer SERVER = new KalmiaServer();

    public static void main(String[] args) {
        try {
            SERVER.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
