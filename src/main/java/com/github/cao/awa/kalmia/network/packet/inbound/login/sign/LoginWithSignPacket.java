package com.github.cao.awa.kalmia.network.packet.inbound.login.sign;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotation.auto.event.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.network.inbound.login.sign.LoginWithSignEvent;
import com.github.cao.awa.kalmia.network.handler.ping.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.security.interfaces.ECPrivateKey;

@AutoSolvedPacket(100003)
@NetworkEventTarget(LoginWithSignEvent.class)
public class LoginWithSignPacket extends Packet<StatelessHandler> {
    @AutoData
    private long uid;
    @AutoData
    private byte[] challengeData;

    // Only support ec to use sign login.
    @Client
    public LoginWithSignPacket(long uid, ECPrivateKey privateKey) {
        this.uid = uid;
        try {
            this.challengeData = Crypto.ecSign(
                    KalmiaEnv.CHALLENGE_DATA,
                    privateKey
            );
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Failed sign the data"
            );
        }
    }

    @Auto
    @Server
    public LoginWithSignPacket(BytesReader reader) {
        super(reader);
    }

    public long uid() {
        return this.uid;
    }

    public byte[] challengeData() {
        return this.challengeData;
    }
}
