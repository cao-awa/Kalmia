package com.github.cao.awa.kalmia.plugin;

import java.util.UUID;

public abstract class Plugin {

    public abstract UUID uuid();

    public abstract void load();

    public abstract void unload();
}
