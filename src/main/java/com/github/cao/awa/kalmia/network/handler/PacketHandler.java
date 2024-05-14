package com.github.cao.awa.kalmia.network.handler;

import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public abstract class PacketHandler<H extends PacketHandler<H>> {
    private static final Logger LOGGER = LogManager.getLogger("PacketHandler");

    public Packet<H> handle(UnsolvedPacket<?> packet) {
        return Manipulate.cast(packet.packet());
    }

    public Packet<H> tryHandle(UnsolvedPacket<?> packet) {
        try {
            return handle(packet);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidPacketException("Unsupported packet '" + packet + "'  in this handler: " + this);
        }
    }

    public abstract void inbound(Packet<H> packet, RequestRouter router);

    public boolean tryInbound(UnsolvedPacket<?> packet, RequestRouter router) {
        if (allowStates().contains(router.getStates())) {
            return Manipulate.supply(() -> {
                                         Packet<H> p = tryHandle(packet);

                                         LOGGER.info("Inbounding packet: {}",
                                                     p.getClass()
                                         );

//                                              Manipulate.action(
//                                                      () ->
                                         inbound(
                                                 p,
                                                 router
                                         );
//                                                      ,
//                                                      Throwable :: printStackTrace
//                                              );

                                         return true;
                                     }
                             )
                             .getOrDefault(false);
        } else {
            return false;
        }
    }

    public abstract Set<RequestState> allowStates();
}
