package com.github.cao.awa.kalmia.event.kalmiagram.handler.launch.done;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.EventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.launch.done.DoneLaunchEvent;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(DoneLaunchEvent.class)
public interface DoneLaunchEventHandler extends EventHandler<DoneLaunchEvent> {

}

