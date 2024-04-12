package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@AutoPlugin(name = "kalmia_client", uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE815")
public class KalmiaClientCore extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaClientCore");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia client core");
    }

    @Override
    public boolean forceRegister() {
        return KalmiaServer.serverBootstrapConfig.getTranslation().getEnable();
    }
}
