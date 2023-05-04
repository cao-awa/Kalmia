package com.github.cao.awa.kalmia.security.cipher.manager;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.Map;

public abstract class KeyManager<T> {
    private final Map<String, T> keys = ApricotCollectionFactor.newHashMap();

    public void add(String field, T key) {
        this.keys.put(field,
                      key
        );
    }

    public void delete(String field) {
        this.keys.remove(field);
    }

    public T get(String field) {
        return this.keys.get(field);
    }

    public boolean has(String field) {
        return get(field) != null;
    }
}
