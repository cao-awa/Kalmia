package com.github.cao.awa.kalmia.database.provider;

public abstract class KeyValueDatabaseProvider {
    private final String path;

    public KeyValueDatabaseProvider(String path) {
        this.path = path;
    }

    public String path() {
        return this.path;
    }
}
