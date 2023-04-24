package com.github.cao.awa.kalmia.database;

public abstract class KeyValueDatabaseProvide {
    private final String path;

    public KeyValueDatabaseProvide(String path) {
        this.path = path;
    }

    public String path() {
        return this.path;
    }
}
