package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.annotation.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoPlugin(
        name = "kalmia_core",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE814"
)
public class KalmiaServerCore extends Plugin {
    @Override
    public void load() {
        System.out.println("Loading kalmia server core");
    }

    @Override
    public void unload() {

    }
}
