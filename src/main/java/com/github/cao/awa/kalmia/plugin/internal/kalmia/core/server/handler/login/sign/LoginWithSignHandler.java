package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.login.sign;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.sign.LoginWithSignEventHandler;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.login.LoginCommon;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestState;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Server
@PluginRegister(name = "kalmia_core")
public class LoginWithSignHandler implements LoginWithSignEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("LoginWithSignHandler");

    @Auto
    @Server
    @Override
    public void handle(RequestRouter router, LoginWithSignPacket packet) {
        LongAndExtraIdentity accessIdentity = packet.accessIdentity();

        boolean verified = false;

        try {
            // TODO
//            verified = ExhaustiveLogin.validate(router) && Crypto.ecVerify(KalmiaEnv.CHALLENGE_DATA,
//                                                                           packet.challengeData(),
//                                                                           (ECPublicKey) Kalmia.SERVER.keypairManager().publicKey()
//            );
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Failed verify the data"
            );
        }

        if (verified) {
            router.setStates(RequestState.AUTHED);

            byte[] token = BytesRandomIdentifier.create(128);

            LoginCommon.login(
                    packet.accessIdentity(),
                    router
            );

            loginSuccess(
                    router,
                    accessIdentity,
                    token,
                    packet.receipt()
            );
        } else {
            loginFailure(
                    router,
                    accessIdentity,
                    "login.failure.unable_to_verify_sign",
                    packet.receipt()
            );
        }
    }
}
