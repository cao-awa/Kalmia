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
import com.github.cao.awa.kalmia.network.packet.request.ping.unstatus.TryPingRequest;
import com.github.cao.awa.kalmia.network.packet.request.ping.unstatus.TryPingResponseRequest;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.aes.UnsolvedHandshakeAesCipherPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.crypto.rsa.pubkey.UnsolvedHandshakeRsaPubkeyPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedClientHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.handshake.hello.UnsolvedServerHelloPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.login.UnsolvedLoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingResponsePacket;

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
        // Ping
        register(
                // -2
                TryPingResponseRequest.ID,
                UnsolvedTryPingResponsePacket :: new
        );
        register(
                // -1
                TryPingRequest.ID,
                UnsolvedTryPingPacket :: new
        );

        // Handshake
        register(
                // 0
                ClientHelloRequest.ID,
                UnsolvedClientHelloPacket :: new
        );
        register(
                // 1
                HandshakeRsaPubkeyRequest.ID,
                UnsolvedHandshakeRsaPubkeyPacket :: new
        );
        register(
                // 2
                HandshakeAesCipherRequest.ID,
                UnsolvedHandshakeAesCipherPacket :: new
        );
        register(
                // 3
                ServerHelloRequest.ID,
                UnsolvedServerHelloPacket :: new
        );

        // Login
        register(4,
                 UnsolvedLoginWithPasswordPacket :: new
        );
    }
}
