package com.github.cao.awa.kalmia.database;

import java.util.Map;
import java.util.function.Supplier;

public abstract class KeyValueBytesDatabase extends KeyValueDatabase<byte[], byte[]> {

    public KeyValueBytesDatabase(Supplier<Map<byte[], byte[]>> cacheDelegate) {
        super(cacheDelegate);
    }
}
