package com.github.cao.awa.kalmia.network.echo;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.packet.ResponsePacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;

import java.util.Map;
import java.util.function.Consumer;

public class EchoManager {
    private final Map<UnsolvedRequestRouter, Map<byte[], Consumer<ResponsePacket>>> responses = ApricotCollectionFactor.newHashMap();

    public void echo(UnsolvedRequestRouter router, byte[] identifier, Consumer<ResponsePacket> action) {
        this.responses.putIfAbsent(router, ApricotCollectionFactor.newHashMap());
        this.responses.get(router).put(identifier, action);
    }

    public void echo(UnsolvedRequestRouter router, byte[] identifier, ResponsePacket packet) {
        Map<byte[], Consumer<ResponsePacket>> actions = this.responses.get(router);
        if (actions == null) {
            return;
        }
        Consumer<ResponsePacket> action = actions.get(identifier);
        if (action == null) {
            return;
        }
        action.accept(packet);
    }
}
