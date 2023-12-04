package com.github.cao.awa.kalmia.plugin;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler;

import java.util.UUID;

public abstract class Plugin {
    private boolean enabled = false;

    @Auto
    public UUID uuid() {
        return KalmiaEnv.pluginFramework.uuid(this);
    }

    public void registerHandler(EventHandler<?> handler) {
        optionalRegisterHandler(handler);
    }

    public void forceRegisterHandler(EventHandler<?> handler) {
        KalmiaEnv.eventFramework.registerHandler(handler,
                                                 this
        );
    }

    public void optionalRegisterHandler(EventHandler<?> handler) {
        KalmiaEnv.eventFramework.optionalRegisterHandler(handler,
                                                         this
        );
    }

    public boolean canLoad() {
        return true;
    }

    public boolean forceRegister() {
        return false;
    }

    public void onLoad() {

    }

    public void onUnload() {

    }

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
