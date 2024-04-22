package com.github.cao.awa.kalmia.config;

import com.github.cao.awa.kalmia.env.KalmiaEnv;

public abstract class KalmiaConfig {
    public static void create(Object target) {
        KalmiaEnv.CONFIG_FRAMEWORK.createConfig(target);
    }
}
