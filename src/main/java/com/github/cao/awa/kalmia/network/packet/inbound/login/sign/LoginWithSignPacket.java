package com.github.cao.awa.kalmia.network.packet.inbound.login.sign;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.login.sign.LoginWithSignEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.stateless.StatelessHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.security.interfaces.ECPrivateKey;

@AutoSolvedPacket(id = 100003, crypto = true)
@NetworkEventTarget(LoginWithSignEvent.class)
public class LoginWithSignPacket extends Packet<StatelessHandler> {
    @AutoData
    @DoNotSet
    private LongAndExtraIdentity uid;
    @AutoData
    @DoNotSet
    private byte[] challengeData;

    // Only support ec to use sign login.
    @Client
    public LoginWithSignPacket(LongAndExtraIdentity uid, ECPrivateKey privateKey) {
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

    @Getter
    public LongAndExtraIdentity accessIdentity() {
        return this.uid;
    }

    @Getter
    public byte[] challengeData() {
        return this.challengeData;
    }
}
