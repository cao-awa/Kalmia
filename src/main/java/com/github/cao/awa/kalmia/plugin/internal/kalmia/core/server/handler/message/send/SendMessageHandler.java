package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SendMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class SendMessageHandler implements SendMessageEventHandler {
    @Server
    @Override
    public void handle(RequestRouter router, SendMessagePacket packet) {
        System.out.println("UID: " + packet.handler()
                                           .getUid());
        System.out.println("SID: " + packet.sessionId());
        System.out.println("IDT: " + Arrays.toString(packet.receipt()));
        System.out.println("MSG: " + packet.msg());

        if (Kalmia.SERVER.sessionManager()
                         .accessible(packet.handler()
                                           .getUid(),
                                     packet.sessionId()
                         )) {
            long seq = Kalmia.SERVER.messageManager()
                                    .send(
                                            packet.sessionId(),
                                            packet.msg()
                                    );

            // Response to client the seq.
            router.send(new SentMessagePacket(seq,
                                              packet.receipt()
            ));
        } else {
            // Unable to access the session.
            System.out.println("www");
        }
    }
}
