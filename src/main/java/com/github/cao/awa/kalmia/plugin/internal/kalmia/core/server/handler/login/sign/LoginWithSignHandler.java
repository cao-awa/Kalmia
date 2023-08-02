package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.login.sign;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotation.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.handler.network.inbound.login.sign.LoginWithSignEventHandler;
import com.github.cao.awa.kalmia.login.LoginCommon;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.success.LoginSuccessPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.interfaces.ECPublicKey;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class LoginWithSignHandler implements LoginWithSignEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("LoginWithSignHandler");

    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, LoginWithSignPacket packet) {
        if (! PublicKeyIdentity.ensurePublicKeySettings(packet.uid(),
                                                        router
        )) {
            return;
        }
        try {
            boolean verified = Crypto.ecVerify(KalmiaEnv.CHALLENGE_DATA,
                                               packet.challengeData(),
                                               (ECPublicKey) Kalmia.SERVER.userManager()
                                                                          .publicKey(packet.uid())
            );

            if (verified) {
                router.setStates(RequestState.AUTHED);
                ((AuthedRequestHandler) router.getHandler()).setUid(packet.uid());

                byte[] token = BytesRandomIdentifier.create(64);

                LoginCommon.login(
                        packet.uid(),
                        router
                );

                router.send(new LoginSuccessPacket(packet.uid(),
                                                   token
                ));
            } else {
            }
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Failed verify the data"
            );
        }
    }
}
