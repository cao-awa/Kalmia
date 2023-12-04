package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.login.sign;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.attack.exhaustive.ExhaustiveLogin;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.login.sign.LoginWithSignEventHandler;
import com.github.cao.awa.kalmia.login.LoginCommon;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState;
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
        long uid = packet.uid();

        if (! PublicKeyIdentity.ensurePublicKeySettings(uid,
                                                        router
        )) {
            return;
        }

        boolean verified = false;

        try {
            verified = ExhaustiveLogin.validate(router) && Crypto.ecVerify(KalmiaEnv.CHALLENGE_DATA,
                                                                           packet.challengeData(),
                                                                           (ECPublicKey) Kalmia.SERVER.userManager()
                                                                                                      .publicKey(uid)
            );
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Failed verify the data"
            );
        }

        if (verified) {
            router.setStates(RequestState.AUTHED);
            ((AuthedRequestHandler) router.getHandler()).setUid(uid);

            byte[] token = BytesRandomIdentifier.create(128);

            LoginCommon.login(
                    packet.uid(),
                    router
            );

            loginSuccess(
                    router,
                    uid,
                    token
            );
        } else {
            loginFailure(
                    router,
                    uid,
                    "login.failure.unable_to_verify_sign"
            );
        }
    }
}
