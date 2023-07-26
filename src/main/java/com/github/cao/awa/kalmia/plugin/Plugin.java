package com.github.cao.awa.kalmia.plugin;

import com.github.cao.awa.kalmia.env.KalmiaEnv;

import java.util.UUID;

public abstract class Plugin {

    public UUID uuid() {
        return KalmiaEnv.pluginFramework.uuid(this);
    }

    public abstract void load();

    public abstract void unload();
}
