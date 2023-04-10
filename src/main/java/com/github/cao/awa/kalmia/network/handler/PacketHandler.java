package com.github.cao.awa.kalmia.network.handler;

import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.exception.InvalidStatusException;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Set;

public abstract class PacketHandler<T extends UnsolvedPacket<?>> {
    public ReadonlyPacket handle(T packet) {
        return packet.toPacket();
    }

    public ReadonlyPacket tryHandle(UnsolvedPacket<?> packet) {
        T t = EntrustEnvironment.cast(packet);
        if (t == null) {
            throw new InvalidPacketException("Unsupported packet '" + packet + "'  in this handler: " + this);
        }
        try {
            return handle(t);
        }catch (Exception e) {
            throw new InvalidPacketException("Unsupported packet '" + t + "'  in this handler: " + this);
        }
    }

    public abstract void inbound(ReadonlyPacket packet, UnsolvedRequestRouter router);

    public void tryInbound(UnsolvedPacket<?> packet, UnsolvedRequestRouter router) {
        if (allowStatus().contains(router.getStatus())) {
            ReadonlyPacket readonly = tryHandle(packet);
            inbound(readonly,
                    router
            );
        } else {
            throw new InvalidStatusException("The router status '" + router.getStatus() + "' are not allowed in this handler, allows: " + allowStatus());
        }
    }

    public abstract Set<RequestStatus> allowStatus();
}
