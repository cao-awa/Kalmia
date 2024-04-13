package com.github.cao.awa.kalmia.server

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import com.github.cao.awa.apricot.resource.loader.ResourceLoader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.io.IOUtil
import com.github.cao.awa.kalmia.config.kalmiagram.server.bootstrap.ServerBootstrapConfig
import com.github.cao.awa.kalmia.constant.KalmiaConstant
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.event.kalmiagram.launch.done.DoneLaunchEvent
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity
import com.github.cao.awa.kalmia.keypair.manager.KeypairManager
import com.github.cao.awa.kalmia.message.cover.processor.MessageProcessor
import com.github.cao.awa.kalmia.message.manager.MessageManager
import com.github.cao.awa.kalmia.network.io.server.KalmiaServerNetworkIo
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.kalmia.session.communal.CommunalSession
import com.github.cao.awa.kalmia.session.listener.SessionListeners
import com.github.cao.awa.kalmia.session.manager.SessionManager
import com.github.cao.awa.kalmia.user.User
import com.github.cao.awa.kalmia.user.manager.UserManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.util.UUID
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import kotlin.collections.MutableList
import kotlin.collections.MutableMap

class KalmiaServer(config: ServerBootstrapConfig) {
    val bootstrapConfig: ServerBootstrapConfig
    private val networkIo: KalmiaServerNetworkIo
    val messageManager: MessageManager
    val userManager: UserManager
    val keypairManager: KeypairManager
    val sessionManager: SessionManager
    private val messageProcessors: MutableMap<UUID, MessageProcessor> = ApricotCollectionFactor.hashMap()
    private val sessionListeners: SessionListeners = SessionListeners()
    private var started: Boolean = false
    private val executor: ExecutorService = Executors.newCachedThreadPool()
    
    fun messageProcessor(id: UUID): MessageProcessor {
        messageProcessors[id].let { throw RuntimeException() }
    }

    init {
        try {
            bootstrapConfig = config

            networkIo = KalmiaServerNetworkIo(this)

            messageManager = MessageManager("data/server/msg")
            userManager = UserManager("data/server/usr")
            keypairManager = KeypairManager("data/server/keypair")
            sessionManager = SessionManager("data/server/session")

            // TODO
            // Just for test
            assert(KalmiaEnv.testUser1 != null)
            userManager.set(0, KalmiaEnv.testUser1)

            assert(KalmiaEnv.testUser2 != null)
            userManager.set(1, KalmiaEnv.testUser2)

            sessionManager.set(CommunalSession.TEST_COMMUNAL_IDENTITY, CommunalSession.TEST_COMMUNAL)
            sessionManager.curSeq(0)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun startup() {
        setupNetwork()
        KalmiaEnv.eventFramework.fireEvent(DoneLaunchEvent())
    }

    fun registerMessageProcessor(processor: MessageProcessor) {
        if (messageProcessors[processor.id()] == null) {
            messageProcessors[processor.id()] = processor

            LOGGER.info("Registered message processor '{}' by id '{}'", processor.javaClass.name, processor.id())
        } else {
            LOGGER.warn(
                "Unable to register message processor '{}' by id '{}', because this id has already used",
                processor.javaClass.name,
                processor.id()
            )
        }
    }

    fun setupNetwork() {
        UnsolvedPacketFactor.register()

        started = true

        task { ->
            try {
                networkIo.start(bootstrapConfig.serverNetwork)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun task(runnable: Runnable) {
        executor.execute(runnable)
    }

    fun useEpoll(): Boolean {
        return true
    }

    fun getRouters(accessIdentity: LongAndExtraIdentity): MutableList<RequestRouter> {
        return networkIo.getRouter(accessIdentity)
    }

    fun login(accessIdentity: LongAndExtraIdentity, router: RequestRouter) {
        networkIo.login(accessIdentity, router)
    }

    fun logout(accessIdentity: LongAndExtraIdentity, router: RequestRouter) {
        networkIo.logout(accessIdentity, router)
        sessionListeners.unsubscribe(router)
    }

    fun logout(router: RequestRouter) {
        networkIo.logout(router.accessIdentity(), router)
    }

    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaServer")
        lateinit var serverBootstrapConfig: ServerBootstrapConfig

        @JvmStatic
        fun setupBootstrapConfig() {
            prepareConfig()

            serverBootstrapConfig = ServerBootstrapConfig.read(
                JSONObject.parse(IOUtil.read(FileReader(KalmiaConstant.SERVER_CONFIG_PATH))),
                KalmiaEnv.DEFAULT_SERVER_BOOTSTRAP_CONFIG
            )

            rewriteConfig(serverBootstrapConfig)
        }

        fun rewriteConfig(bootstrapConfig: ServerBootstrapConfig) {
            LOGGER.info("Rewriting server config");

            IOUtil.write(
                FileWriter(KalmiaConstant.SERVER_CONFIG_PATH),
                bootstrapConfig.toJSON().toString(JSONWriter.Feature.PrettyFormat)
            )
        }

        fun prepareConfig() {
            LOGGER.info("Preparing server config")

            val configFile = File(KalmiaConstant.SERVER_CONFIG_PATH)

            configFile.getParentFile().mkdirs()

            if (!configFile.isFile()) {
                IOUtil.write(
                    FileWriter(configFile),
                    IOUtil.read(InputStreamReader(ResourceLoader.stream(KalmiaConstant.SERVER_DEFAULT_CONFIG_PATH)))
                )
            }
        }
    }
}