package com.github.cao.awa.kalmia.network.handler;

import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

public abstract class PacketHandler<T extends UnsolvedPacket<?>> {
    public abstract ReadonlyPacket handle(T packet);

    public ReadonlyPacket tryHandle(UnsolvedPacket<?> packet) {
        T t = EntrustEnvironment.cast(packet);
        if (t == null) {
            throw new InvalidPacketException("Not supported to handle in this handler");
        }
        return handle(t);
    }

    public abstract void inbound(ReadonlyPacket packet, UnsolvedRequestRouter router);

    public void tryInbound(UnsolvedPacket<?> packet, UnsolvedRequestRouter router) {
        ReadonlyPacket readonly = tryHandle(packet);
        inbound(readonly, router);
    }
}