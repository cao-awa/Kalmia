package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.test;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.annotation.auto.AutoPlugin;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.plugin.Plugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@AutoPlugin(
        name = "kalmia_test",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE816"
)
public class KalmiaTest extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaTestPlugin");

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia test plugin");
    }

    @Override
    public boolean canLoad() {
        return ! Kalmia.bootstrapConfig.translation()
                                       .enable();
    }
}
