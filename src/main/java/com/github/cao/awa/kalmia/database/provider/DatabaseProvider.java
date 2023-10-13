package com.github.cao.awa.kalmia.database.provider;

import com.github.cao.awa.kalmia.database.KeyValueDatabase;
import com.github.cao.awa.kalmia.database.provider.leveldb.LevelDbProvider;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingFunction;

public class DatabaseProvider {
    public static ExceptingFunction<String, KeyValueDatabase> defaultProvider = LevelDbProvider :: new;

    public static KeyValueDatabase kv(String path) throws Exception {
        return defaultProvider.apply(path);
    }
}
