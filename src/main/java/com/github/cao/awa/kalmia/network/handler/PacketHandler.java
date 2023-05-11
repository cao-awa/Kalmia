package com.github.cao.awa.kalmia.network.handler;

import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.exception.InvalidStatusException;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public abstract class PacketHandler<H extends PacketHandler<H>> {
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

    public void tryInbound(UnsolvedPacket<?> packet, RequestRouter router) {
        if (allowStatus().contains(router.getStatus())) {
            Packet<H> readonly = tryHandle(packet);
            inbound(readonly,
                    router
            );
        } else {
            throw new InvalidStatusException("The router status '" + router.getStatus() + "' are not allowed in this handler, allows: " + allowStatus());
        }
    }

    public abstract Set<RequestStatus> allowStatus();
}
