package com.github.cao.awa.kalmia.database;

import com.github.cao.awa.kalmia.database.cache.KeyValueCache;

import java.util.Map;
import java.util.function.Supplier;

public abstract class KeyValueDatabase<K, V> {
    private final KeyValueCache<K, V> cache;

    public KeyValueDatabase(Supplier<Map<K, V>> cacheDelegate) {
        this.cache = new KeyValueCache<>(
                cacheDelegate.get(),
                this
        );
    }

    public KeyValueCache<K, V> cache() {
        return this.cache;
    }

    public abstract void put(K key, V value);

    public abstract V get(K key);

    public abstract void remove(K key);

    public boolean close() {
        return true;
    }
}
