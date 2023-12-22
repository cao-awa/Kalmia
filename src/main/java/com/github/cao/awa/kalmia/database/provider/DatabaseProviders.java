package com.github.cao.awa.kalmia.database.provider;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.database.KeyValueBytesDatabase;
import com.github.cao.awa.kalmia.database.key.BytesKey;
import com.github.cao.awa.kalmia.database.provider.leveldb.LevelDbProvider;

import java.util.Map;
import java.util.function.Supplier;

public class DatabaseProviders {
    public static KeyValueBytesDatabase bytes(String path) throws Exception {
        return new LevelDbProvider(
                ApricotCollectionFactor :: hashMap,
                path
        );
    }

    public static KeyValueBytesDatabase bytes(Supplier<Map<BytesKey, byte[]>> delegate, String path) throws Exception {
        return new LevelDbProvider(
                delegate,
                path
        );
    }
}
