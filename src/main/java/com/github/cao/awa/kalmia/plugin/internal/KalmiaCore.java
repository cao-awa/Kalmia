package com.github.cao.awa.kalmia.plugin.internal;

import com.github.cao.awa.apricot.annotation.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;

import java.util.UUID;

@AutoPlugin
public class KalmiaCore extends Plugin {
    private static final UUID ID = UUID.fromString("C942B874-2E65-CCB4-8B8C-0C743E7BE814");

    @Override
    public UUID uuid() {
        return ID;
    }

    @Override
    public void load() {
        System.out.println("Loading kalmia core");
    }

    @Override
    public void unload() {

    }
}
