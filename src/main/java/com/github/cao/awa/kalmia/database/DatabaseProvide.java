package com.github.cao.awa.kalmia.database;

import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingFunction;

public class DatabaseProvide {
    public static ExceptingFunction<String, KeyValueDatabase> defaultProvider = LevelDbProvide :: new;

    public static KeyValueDatabase kv(String path) throws Exception {
        return defaultProvider.apply(path);
    }
}
