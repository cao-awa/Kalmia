package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.test;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.annotation.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@Auto
@Server
@AutoPlugin(
        name = "kalmia_test",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE816"
)
public class KalmiaTest extends Plugin {
    @Override
    public void load() {
        System.out.println("Loading kalmia test");
    }

    @Override
    public void unload() {

    }
}
