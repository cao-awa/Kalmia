package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.message.cover.processor.coloregg.MeowMessageProcessor;
import com.github.cao.awa.kalmia.message.cover.processor.time.TimeMessageProcessor;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Server
@AutoPlugin(name = "kalmia_core", uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE814")
public class KalmiaServerCore extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaServerCore");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia server core");

        Kalmia.SERVER.registerMessageProcessor(new MeowMessageProcessor());
        Kalmia.SERVER.registerMessageProcessor(new TimeMessageProcessor());
    }

    @Override
    public boolean canLoad() {
        return !KalmiaServer.serverBootstrapConfig.getTranslation().getEnable();
    }
}
