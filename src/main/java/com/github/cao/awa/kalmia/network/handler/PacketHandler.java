package com.github.cao.awa.kalmia.network.handler;

import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public abstract class PacketHandler<H extends PacketHandler<H>> {
    private static final Logger LOGGER = LogManager.getLogger("PacketHandler");

    public Packet<H> handle(UnsolvedPacket<?> packet) {
        return EntrustEnvironment.cast(packet.packet());
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
        if (allowStates().contains(router.getStatus())) {
            return EntrustEnvironment.get(() -> {
                                              Packet<H> p = tryHandle(packet);

                                              LOGGER.debug("Inbounding packet: {}",
                                                           p.getClass()
                                              );

                                              EntrustEnvironment.trys(
                                                      () -> inbound(
                                                              p,
                                                              router
                                                      ),
                                                      Throwable :: printStackTrace
                                              );

                                              return true;
                                          },
                                          false
            );
        } else {
            return false;
        }
    }

    public abstract Set<RequestState> allowStates();
}
