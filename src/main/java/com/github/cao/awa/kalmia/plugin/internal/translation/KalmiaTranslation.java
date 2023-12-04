package com.github.cao.awa.kalmia.plugin.internal.translation;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.server.KalmiaServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@AutoPlugin(
        name = "kalmia_translation",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE817"
)
public class KalmiaTranslation extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaTranslation");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia translation");
    }

    @Override
    public boolean canLoad() {
        return KalmiaServer.serverBootstrapConfig.translation()
                                                 .enable();
    }
}
