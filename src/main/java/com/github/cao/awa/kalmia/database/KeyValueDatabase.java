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

    public void put(K key1, K key2, V value) {
        // Redundancy method.
    }

    public void put(K key1, K key2, K key3, V value) {
        // Redundancy method.
    }

    public abstract V get(K key);

    public V get(K key1, K key2) {
        // Redundancy method.
        return null;
    }

    public V get(K key, K key2, K key3) {
        // Redundancy method.
        return null;
    }

    public abstract void remove(K key);

    public void remove(K key1, K key2) {
        // Redundancy method.
    }

    public void remove(K key1, K key2, K key3) {
        // Redundancy method.
    }

    public boolean close() {
        return true;
    }
}
