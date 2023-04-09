package com.github.cao.awa.kalmia.network.packet.factor.unsolve;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes.UnsolvedHandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedClientHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.rsa.pubkey.UnsolvedHandshakeRsaPubkeyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedServerHelloPacket;

import java.util.Map;
import java.util.function.Function;

public class UnsolvedPacketFactor {
    private static final Map<Long, Function<byte[], UnsolvedPacket<?>>> factories = ApricotCollectionFactor.newHashMap();

    public static UnsolvedPacket<?> create(long id, byte[] data) {
        Function<byte[], UnsolvedPacket<?>> creator = factories.get(id);
        if (creator == null) {
            throw new InvalidPacketException();
        }
        return creator.apply(data);
    }

    public static void register(long id, Function<byte[], UnsolvedPacket<?>> creator) {
        factories.put(id,
                      creator
        );
    }

    public static void register() {
        UnsolvedPacketFactor.register(0,
                                      UnsolvedClientHelloPacket :: new
        );
        UnsolvedPacketFactor.register(1,
                                      UnsolvedHandshakeRsaPubkeyPacket :: new
        );
        UnsolvedPacketFactor.register(2,
                                      UnsolvedHandshakeAesCipherPacket :: new
        );
        UnsolvedPacketFactor.register(3,
                                      UnsolvedServerHelloPacket :: new
        );
    }
}
