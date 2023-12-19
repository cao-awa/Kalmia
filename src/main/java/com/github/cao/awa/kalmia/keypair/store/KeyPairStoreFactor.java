package com.github.cao.awa.kalmia.keypair.store;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.TriFunction;

import java.util.Map;

public class KeyPairStoreFactor {
    private static final Map<Integer, TriFunction<PureExtraIdentity, byte[], byte[], KeyPairStore>> factories = ApricotCollectionFactor.hashMap();

    public static void register(int id, TriFunction<PureExtraIdentity, byte[], byte[], KeyPairStore> factor) {
        factories.put(id,
                      factor
        );
    }

    public static KeyPairStore create(int id, PureExtraIdentity identity, byte[] publicKey, byte[] privateKey) {
        return factories.get(id)
                        .apply(identity,
                               publicKey,
                               privateKey
                        );
    }
}
