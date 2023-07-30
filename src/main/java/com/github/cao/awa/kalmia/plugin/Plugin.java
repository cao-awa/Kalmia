package com.github.cao.awa.kalmia.plugin;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.handler.EventHandler;

import java.util.UUID;

public abstract class Plugin {
    private boolean enabled = true;

    @Auto
    public UUID uuid() {
        return KalmiaEnv.pluginFramework.uuid(this);
    }

    public void registerHandler(EventHandler<?> handler) {

    }

    public abstract void onEnable();

    public abstract void onDisable();

    public final void enable() {
        onEnable();

        this.enabled = true;
    }

    public final void disable() {
        onDisable();

        this.enabled = false;
    }

    public final boolean enabled() {
        return this.enabled;
    }
}
