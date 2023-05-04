package com.github.cao.awa.kalmia.network.packet.factor.unsolve;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.exception.InvalidPacketException;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.dual.invalid.operation.OperationInvalidPacket;
import com.github.cao.awa.kalmia.network.packet.dual.ping.unstatus.TryPingPacket;
import com.github.cao.awa.kalmia.network.packet.dual.ping.unstatus.TryPingResponsePacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.invalid.operation.UnsolvedOperationInvalidPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingPacket;
import com.github.cao.awa.kalmia.network.packet.unsolve.ping.UnsolvedTryPingResponsePacket;

import java.util.Map;
import java.util.function.Function;

public class UnsolvedPacketFactor {
    private static final Map<Long, Function<byte[], UnsolvedPacket<?>>> factories = ApricotCollectionFactor.newHashMap();

    public static UnsolvedPacket<?> create(long id, byte[] data, byte[] receipt) {
        Function<byte[], UnsolvedPacket<?>> creator = factories.get(id);
        if (creator == null) {
            throw new InvalidPacketException();
        }
        return creator.apply(data)
                      .receipt(receipt);
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
                // 4
                TryPingPacket.ID,
                UnsolvedTryPingPacket :: new
        );
        register(
                // 5
                TryPingResponsePacket.ID,
                UnsolvedTryPingResponsePacket :: new
        );

        // Invalid operation
        register(
                OperationInvalidPacket.ID,
                UnsolvedOperationInvalidPacket :: new
        );
    }
}
