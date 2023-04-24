package com.github.cao.awa.kalmia.database;

public interface KeyValueDatabase {
    void put(byte[] key, byte[] value);

    byte[] get(byte[] key);
}
