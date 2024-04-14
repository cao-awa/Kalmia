package com.github.cao.awa.kalmia.bootstrap

import com.github.cao.awa.kalmia.client.KalmiaClient
import com.github.cao.awa.kalmia.client.polling.PollingClient
import com.github.cao.awa.kalmia.constant.KalmiaConstant
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.env.KalmiaTranslationEnv
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.keypair.pair.ec.EcKeyPair
import com.github.cao.awa.kalmia.keypair.pair.empty.EmptyKeyPair
import com.github.cao.awa.kalmia.keypair.pair.rsa.RsaKeyPair
import com.github.cao.awa.kalmia.keypair.store.KeyPairStoreFactor
import com.github.cao.awa.kalmia.message.cover.CoverMessage
import com.github.cao.awa.kalmia.message.deleted.DeletedMessage
import com.github.cao.awa.kalmia.message.factor.MessageFactor
import com.github.cao.awa.kalmia.message.user.UserMessage
import com.github.cao.awa.kalmia.network.packet.inbound.handshake.hello.client.ClientHelloPacket
import com.github.cao.awa.kalmia.server.KalmiaServer
import com.github.cao.awa.kalmia.session.communal.CommunalSession
import com.github.cao.awa.kalmia.session.duet.DuetSession
import com.github.cao.awa.kalmia.session.factor.SessionFactor
import com.github.cao.awa.kalmia.session.group.GroupSession
import com.github.cao.awa.kalmia.user.DefaultUser
import com.github.cao.awa.kalmia.user.UselessUser
import com.github.cao.awa.kalmia.user.factor.UserFactor
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Kalmia {
    private val LOGGER: Logger = LogManager.getLogger("Kalmia")
    lateinit var SERVER: KalmiaServer
    lateinit var CLIENT: KalmiaClient

    @JvmStatic
    fun main(args: Array<String>) {
        try {
            startServer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun startServer() {
        setupEnvironment()

        LOGGER.info("Starting kalmia server")

        SERVER = KalmiaServer(KalmiaServer.serverBootstrapConfig)

        LOGGER.info("Setup kalmia server")

        KalmiaEnv.setupServer()

        if (KalmiaServer.serverBootstrapConfig.translation.enable) {
            KalmiaTranslationEnv.setupFrameworks()
        }

        setupTest()

        SERVER.startup()
    }

    @JvmStatic
    fun startClient() {
        setupEnvironment()

        LOGGER.info("Starting kalmia client")

        CLIENT = KalmiaClient(KalmiaClient.clientBootstrapConfig)

        LOGGER.info("Setup kalmia client")

        KalmiaEnv.setupClient()

        CLIENT.activeCallback { router ->
            router.send(ClientHelloPacket(KalmiaConstant.STANDARD_REQUEST_PROTOCOL, "KalmiaWww v1.0.1"))
        }

        PollingClient.CLIENT = PollingClient(CLIENT)

        CLIENT.connect()
    }

    fun setupTest() {
        val keys: MutableSet<PureExtraIdentity> = SERVER.userManager.keyStores(KalmiaEnv.testUser1.identity())

        keys.add(KalmiaEnv.testKeypair0.identity())
        keys.add(KalmiaEnv.testKeypair1.identity())

        SERVER.userManager.keyStores(KalmiaEnv.testUser1.identity(), keys)

        SERVER.keypairManager.set(KalmiaEnv.testKeypair0.identity(), KalmiaEnv.testKeypair0)

        SERVER.keypairManager.set(KalmiaEnv.testKeypair1.identity(), KalmiaEnv.testKeypair1)
    }

    private fun setupEnvironment() {
        try {
            KalmiaServer.setupBootstrapConfig()
            KalmiaClient.setupBootstrapConfig()
        } catch (_: Exception) {

        }

        UserFactor.register(-1, UselessUser::create)
        UserFactor.register(0, DefaultUser::create)

        MessageFactor.register(-1, DeletedMessage::create)
        MessageFactor.register(2, UserMessage::create)
        MessageFactor.register(5, CoverMessage::create)

        KeyPairStoreFactor.register(0, ::RsaKeyPair)
        KeyPairStoreFactor.register(1, ::EcKeyPair)
        KeyPairStoreFactor.register(123, ::EmptyKeyPair)

        SessionFactor.register(1, DuetSession::create)
        SessionFactor.register(2, CommunalSession::create)
        SessionFactor.register(3, GroupSession::create)
    }
}
