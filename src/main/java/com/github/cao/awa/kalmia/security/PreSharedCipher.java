package com.github.cao.awa.kalmia.security;

public abstract class PreSharedCipher<T> {
    public abstract T cipher();

    public abstract String key();
}
