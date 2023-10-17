package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.annotation.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.disconnect.TryDisconnectHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@AutoPlugin(
        name = "kalmia_uni_core",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE813"
)
public class KalmiaUniCore extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaUniCore");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia uni core");

        registerHandler(new TryDisconnectHandler());
    }

    @Override
    public void onUnload() {

    }
}
