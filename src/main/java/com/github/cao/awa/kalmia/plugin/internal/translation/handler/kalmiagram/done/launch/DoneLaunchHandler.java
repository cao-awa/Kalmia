package com.github.cao.awa.kalmia.plugin.internal.translation.handler.kalmiagram.done.launch;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.launch.done.DoneLaunchEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.launch.done.DoneLaunchEvent;

@Auto
@PluginRegister(name = "kalmia_translation")
public class DoneLaunchHandler implements DoneLaunchEventHandler {
    @Override
    public void handle(DoneLaunchEvent event) {
        System.out.println("Done launch");
    }
}
