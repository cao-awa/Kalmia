package com.github.cao.awa.kalmia.database.cache;

import com.github.cao.awa.kalmia.database.KeyValueDatabase;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class KeyValueCache<K, V> {
    private final Map<K, V> cacheMap;
    private final KeyValueDatabase<K, V> database;

    public KeyValueCache(Map<K, V> cacheMap, KeyValueDatabase<K, V> database) {
        this.cacheMap = cacheMap;
        this.database = database;
    }

    public V get(K key, Function<K, V> getter) {
        V result = this.cacheMap.get(key);
        if (result == null) {
            result = getter.apply(key);
            cache(
                    key,
                    result
            );
        }
        return result;
    }

    public V cache(K key, V value) {
        this.cacheMap.put(
                key,
                value
        );
        return value;
    }

    public V expire(K key) {
        return this.cacheMap.remove(key);
    }

    public V update(K key, V value, BiConsumer<K, V> updater) {
        updater.accept(
                key,
                value
        );
        return cache(
                key,
                value
        );
    }

    public V delete(K key, Consumer<K> deleter) {
        deleter.accept(key);
        return expire(key);
    }

    public void clear() {
        this.cacheMap.clear();
    }
}
