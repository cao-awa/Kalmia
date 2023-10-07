package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.send;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.handler.network.inbound.message.send.SendMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SendMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.send.SentMessagePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.session.duet.DuetSession;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.Arrays;
import java.util.List;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class SendMessageHandler implements SendMessageEventHandler {
    @Server
    @Override
    public void handle(RequestRouter router, SendMessagePacket packet) {
        System.out.println("UID: " + packet.handler()
                                           .uid());
        System.out.println("SID: " + packet.sessionId());
        System.out.println("IDT: " + Arrays.toString(packet.receipt()));
        System.out.println("MSG: " + packet.msg());


        DuetSession session = (DuetSession) Kalmia.SERVER.sessionManager()
                                                         .session(packet.sessionId());

        if (session == null) {
            return;
        }

        if (session.accessible(packet.handler()
                                     .uid())) {
            long seq = Kalmia.SERVER.messageManager()
                                    .send(
                                            packet.sessionId(),
                                            packet.msg()
                                    );

            List<RequestRouter> targetRouters = Kalmia.SERVER.getRouter(session.opposite(packet.handler()
                                                                                               .uid()));

            if (targetRouters != null) {
                targetRouters.forEach(targetRouter -> {
                    targetRouter.send(
                            new NewMessageNoticePacket(packet.sessionId(),
                                                       seq,
                                                       packet.msg()
                            ));
                });
            }

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
