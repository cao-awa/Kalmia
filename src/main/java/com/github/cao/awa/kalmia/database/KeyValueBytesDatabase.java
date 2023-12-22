package com.github.cao.awa.kalmia.database;

import com.github.cao.awa.kalmia.database.key.BytesKey;

import java.util.Map;
import java.util.function.Supplier;

public abstract class KeyValueBytesDatabase extends KeyValueDatabase<BytesKey, byte[]> {

    public KeyValueBytesDatabase(Supplier<Map<BytesKey, byte[]>> cacheDelegate) {
        super(cacheDelegate);
    }
}
