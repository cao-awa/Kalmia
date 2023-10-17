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
        KalmiaEnv.eventFramework.registerHandler(handler,
                                                 this
        );
    }

    public abstract void onLoad();

    public abstract void onUnload();

    public final void load() {
        onLoad();

        this.enabled = true;
    }

    public final void unload() {
        onUnload();

        this.enabled = false;
    }

    public final boolean enabled() {
        return this.enabled;
    }
}
