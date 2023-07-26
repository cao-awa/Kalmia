package com.github.cao.awa.kalmia.user.key;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.Map;
import java.util.function.BiFunction;

public class ServerKeyPairStoreFactor {
    private static final Map<Integer, BiFunction<byte[], byte[], ServerKeyPairStore>> factories = ApricotCollectionFactor.hashMap();

    public static void register(int id, BiFunction<byte[], byte[], ServerKeyPairStore> factor) {
        factories.put(id,
                      factor
        );
    }

    public static ServerKeyPairStore create(int id, byte[] publicKey, byte[] privateKey) {
        return factories.get(id)
                        .apply(publicKey,
                               privateKey
                        );
    }
}
