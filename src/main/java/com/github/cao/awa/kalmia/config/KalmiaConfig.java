package com.github.cao.awa.kalmia.config;

import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.env.KalmiaEnv;

import java.lang.reflect.Field;

public abstract class KalmiaConfig {
    public static void create(Object target) {
        KalmiaEnv.CONFIG_FRAMEWORK.createConfig(target);
    }

    public static void createEntry(ConfigEntry<?> target, Field field) {
        KalmiaEnv.CONFIG_FRAMEWORK.createEntry(target,
                                               field
        );
    }
}
