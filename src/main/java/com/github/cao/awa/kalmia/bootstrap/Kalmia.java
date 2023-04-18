package com.github.cao.awa.kalmia.bootstrap;

import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.KalmiaServer;
import com.github.cao.awa.kalmia.event.network.login.password.LoginWithPasswordEvent;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Kalmia {
    private static final Logger LOGGER = LogManager.getLogger("Kalmia");
    public static final KalmiaServer SERVER = new KalmiaServer();

    public static void main(String[] args) {
        try {
//            LoginWithPasswordEvent.subscribe(((packet, router, handler) -> {
//                System.out.println("Cancel login");
//
//                // Don't do that if not absolutely necessary.
//                EventController.cancel();
//            }));

            LoginWithPasswordEvent.subscribe((packet, router, handler) -> {
                LOGGER.info("Kalmia event listener detected a login: " + packet.getUid() + " with password: " + Mathematics.radix(Mathematics.toBytes(MessageDigger.digest(packet.getPassword(),
                                                                                                                                                                           MessageDigger.Sha3.SHA_512
                                                                                                                                                      ),
                                                                                                                                                      16
                                                                                                                                  ),
                                                                                                                                  36
                ));
            });

            SERVER.startup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
