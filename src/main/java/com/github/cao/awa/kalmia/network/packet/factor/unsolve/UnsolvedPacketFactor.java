package com.github.cao.awa.kalmia.network.packet.factor.unsolve;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.aes.HandshakeAesCipherRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.crypto.rsa.pubkey.HandshakeRsaPubkeyRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ClientHelloRequest;
import com.github.cao.awa.kalmia.network.packet.request.handshake.hello.ServerHelloRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes.UnsolvedHandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedClientHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.rsa.pubkey.UnsolvedHandshakeRsaPubkeyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedServerHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.UnsolvedLoginWithPasswordPacket;

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

    public static void register(byte[] id, Function<byte[], UnsolvedPacket<?>> creator) {
        factories.put(SkippedBase256.readLong(new BytesReader(id)),
                      creator
        );
    }

    public static void register() {
        // Handshake
        UnsolvedPacketFactor.register(ClientHelloRequest.ID,
                                      UnsolvedClientHelloPacket :: new
        );
        UnsolvedPacketFactor.register(HandshakeRsaPubkeyRequest.ID,
                                      UnsolvedHandshakeRsaPubkeyPacket :: new
        );
        UnsolvedPacketFactor.register(HandshakeAesCipherRequest.ID,
                                      UnsolvedHandshakeAesCipherPacket :: new
        );
        UnsolvedPacketFactor.register(ServerHelloRequest.ID,
                                      UnsolvedServerHelloPacket :: new
        );

        // Login
        UnsolvedPacketFactor.register(4,
                                      UnsolvedLoginWithPasswordPacket :: new
        );
    }
}
