package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.disconnect.TryDisconnectHandler;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.ping.TryPingHandler;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.ping.TryPingResponseHandler;
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
        registerHandler(new TryPingHandler());
        registerHandler(new TryPingResponseHandler());
    }
}
