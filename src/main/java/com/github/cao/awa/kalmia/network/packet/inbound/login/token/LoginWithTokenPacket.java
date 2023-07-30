package com.github.cao.awa.kalmia.network.packet.inbound.login.token;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.network.inbound.login.token.LoginWithTokenEvent;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

@AutoSolvedPacket(100002)
@NetworkEventTarget(LoginWithTokenEvent.class)
public class LoginWithTokenPacket extends Packet<StatelessHandler> {
    @AutoData
    private long uid;
    @AutoData
    private byte[] token;

    @Client
    public LoginWithTokenPacket(long uid, byte[] token) {
        this.uid = uid;
        this.token = token;
    }

    @Auto
    @Server
    public LoginWithTokenPacket(BytesReader reader) {
        super(reader);
    }

    public long uid() {
        return this.uid;
    }

    public byte[] token() {
        return this.token;
    }
}
