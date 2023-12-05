package com.github.cao.awa.kalmia.plugin.internal.kalmia.core.client.handler.handshake.hello.server;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.plugin.PluginRegister;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.handshake.hello.server.ServerHelloEventHandler;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.server.ServerHelloPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.password.LoginWithPasswordPacket;
import com.github.cao.awa.kalmia.network.packet.inbound.login.sign.LoginWithSignPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.kalmia.status.RequestState;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@Client
@PluginRegister(name = "kalmia_client")
public class ServerHelloHandler implements ServerHelloEventHandler {
    private static final Logger LOGGER = LogManager.getLogger("ServerHelloHandler");

    @Auto
    @Client
    @Override
    public void handle(RequestRouter router, ServerHelloPacket packet) {
        LOGGER.info("Server Hello!");

        byte[] provideCipher = router.decode(packet.testKey());

        LOGGER.debug("Server Sent Hello: " + Mathematics.radix(MessageDigger.digest(provideCipher,
                                                                                    MessageDigger.Sha3.SHA_512
                                                               ),
                                                               16,
                                                               36
        ));
        LOGGER.debug("Server Provide Hello: " + Mathematics.radix(packet.testSha(),
                                                                  36
        ));

        if (packet.iv().length == 16) {
            LOGGER.debug("Server IV: " + Mathematics.radix(router.decode(packet.iv()),
                                                           36
            ));

            router.setIv(router.decode(packet.iv()));
        } else {
            LOGGER.debug("Server IV: DEFAULT IV");

            router.setIv(Crypto.defaultIv());
        }

        // Prepare authed status to enable LoginHandler.
        router.setStates(RequestState.AUTH);

        // TODO
        //     Try login(will delete in releases).
        router.send(new LoginWithPasswordPacket(1,
                                                "123456"
        ));
    }

    public static void user1Login(RequestRouter router) throws Exception {
        router.send(new LoginWithSignPacket(
                1,
                Crypto.decodeEcPrikey(
                        Crypto.aesDecrypt(KalmiaEnv.testUser1.keyPair()
                                                             .privateKey(),
                                          KalmiaEnv.testUer1AesCipher
                        )
                )
        ));
    }

    public static void user2Login(RequestRouter router) throws Exception {
        router.send(new LoginWithSignPacket(
                2,
                Crypto.decodeEcPrikey(
                        Crypto.aesDecrypt(KalmiaEnv.testUser2.keyPair()
                                                             .privateKey(),
                                          KalmiaEnv.testUer2AesCipher
                        )
                )
        ));
    }
}
