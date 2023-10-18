package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.message.delete;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.delete.DeleteMessageEventHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeleteMessagePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.delete.DeletedMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class DeleteMessageHandler implements DeleteMessageEventHandler {
    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, DeleteMessagePacket packet) {
        System.out.println("UID: " + packet.handler()
                                           .uid());
        System.out.println("SID: " + packet.sessionId());
        System.out.println("SEQ: " + packet.seq());

        Kalmia.SERVER.messageManager()
                     .delete(packet.sessionId(),
                             packet.seq()
                     );

        // Response to client the seq.
        router.send(new DeletedMessagePacket(packet.sessionId(),
                                             packet.seq()
        ));

//        Kalmia.SERVER.messageManager().operation(123, (s, m) -> {
//            System.out.println("---");
//            if (m instanceof PlainMessage plain) {
//                System.out.println(s + ": " + plain.getMsg());
//            } else {
//                System.out.println(s + ": " + "<DELETED>");
//            }
//        });
    }
}
