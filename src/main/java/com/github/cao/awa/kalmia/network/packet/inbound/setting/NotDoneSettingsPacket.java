package com.github.cao.awa.kalmia.network.packet.inbound.setting;

import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@NoArgsConstructor
@AutoSolvedPacket(id = 123456, crypto = true)
public class NotDoneSettingsPacket extends Packet<StatelessHandler> {
    private static final Logger LOGGER = LogManager.getLogger("NotDoneSettingsPacket");

    @AutoData
    public String settingName;

    @Server
    public NotDoneSettingsPacket(String settingName) {
        this.settingName = settingName;
    }

    @Client
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {
        LOGGER.info("Settings {} not done",
                    this.settingName
        );
    }
}
