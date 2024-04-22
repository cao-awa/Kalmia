package com.github.cao.awa.kalmia.plugin;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler;

import java.util.UUID;

public abstract class Plugin {
    private boolean enabled = false;

    @Auto
    public UUID uuid() {
        return KalmiaEnv.PLUGIN_FRAMEWORK.uuid(this);
    }

    public void registerHandler(EventHandler<?> handler) {
        optionalRegisterHandler(handler);
    }

    public void registerHandlers(EventHandler<?>... handlers) {
        for (EventHandler<?> handler : handlers) {
            registerHandler(handler);
        }
    }

    public void forceRegisterHandler(EventHandler<?> handler) {
        KalmiaEnv.EVENT_FRAMEWORK.registerHandler(handler,
                                                  this
        );
    }

    public void optionalRegisterHandler(EventHandler<?> handler) {
        KalmiaEnv.EVENT_FRAMEWORK.optionalRegisterHandler(handler,
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
