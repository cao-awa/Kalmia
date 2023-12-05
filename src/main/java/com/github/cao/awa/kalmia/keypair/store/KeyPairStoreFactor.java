package com.github.cao.awa.kalmia.keypair.store;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.Map;
import java.util.function.BiFunction;

public class KeyPairStoreFactor {
    private static final Map<Integer, BiFunction<byte[], byte[], KeyPairStore>> factories = ApricotCollectionFactor.hashMap();

    public static void register(int id, BiFunction<byte[], byte[], KeyPairStore> factor) {
        factories.put(id,
                      factor
        );
    }

    public static KeyPairStore create(int id, byte[] publicKey, byte[] privateKey) {
        return factories.get(id)
                        .apply(publicKey,
                               privateKey
                        );
    }
}
