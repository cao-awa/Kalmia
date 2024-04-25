package com.github.cao.awa.kalmia.config.instance;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

public class ConfigEntry<T> {
    public static final ConfigEntry<?> ENTRY = new ConfigEntry<>();
    private String key;
    private T value;

    public ConfigEntry(Class<T> specifyType) {

    }

    public ConfigEntry() {

    }

    public static <X> ConfigEntry<X> entry() {
        return EntrustEnvironment.cast(ENTRY);
    }

    public String key() {
        return this.key;
    }

    public T get() {
        return this.value;
    }

    public ConfigEntry<T> update(T value) {
        this.value = value;
        return this;
    }
}
