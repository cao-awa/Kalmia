package com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.launch;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.launch.done.DoneLaunchEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.launch.done.DoneLaunchEvent;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.EventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@PluginRegister(name = "kalmia_eventbus")
public class DoneLaunchEventBus extends EventBus<DoneLaunchEventBusHandler> implements DoneLaunchEventHandler {
    @Override
    public void handle(DoneLaunchEvent event) {
        trigger(DoneLaunchEventBusHandler :: handle);
    }
}
