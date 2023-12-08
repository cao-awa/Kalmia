package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingResponsePacket;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.KalmiaEventBus;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.disconnect.TryDisconnectHandler;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.resource.write.RequestNextResourceShardHandler;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.uni.handler.resource.write.WriteResourceHandler;
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

        registerHandlers(
                new WriteResourceHandler(),
                new RequestNextResourceShardHandler()
        );

        KalmiaEventBus.tryPing.trigger((router, receipt, startTime) -> {
            router.send(new TryPingResponsePacket(startTime,
                                                  receipt
            ));
        });

        KalmiaEventBus.tryPingResponse.trigger((router, receipt, startTime) -> {
            double responseMillions = TimeUtil.processMillion(startTime);
            System.out.println("Ping: " + responseMillions + "ms");
        });
    }
}
