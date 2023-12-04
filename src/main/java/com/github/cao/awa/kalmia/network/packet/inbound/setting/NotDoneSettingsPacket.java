package com.github.cao.awa.kalmia.network.packet.inbound.setting;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@AutoSolvedPacket(id = 123456)
public class NotDoneSettingsPacket extends Packet<StatelessHandler> {
    private static final Logger LOGGER = LogManager.getLogger("NotDoneSettingsPacket");

    @AutoData
    public String settingName;

    @Server
    public NotDoneSettingsPacket(String settingName) {
        this.settingName = settingName;
    }

    @Auto
    @Client
    public NotDoneSettingsPacket(BytesReader reader) {
        super(reader);
    }

    @Client
    @Override
    public void inbound(RequestRouter router, StatelessHandler handler) {
        LOGGER.info("Settings {} not done",
                    this.settingName
        );
    }
}
