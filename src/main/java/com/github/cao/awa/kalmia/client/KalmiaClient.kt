package com.github.cao.awa.kalmia.client

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import com.github.cao.awa.apricot.resource.loader.ResourceLoader
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor
import com.github.cao.awa.apricot.util.encryption.Crypto
import com.github.cao.awa.apricot.util.io.IOUtil
import com.github.cao.awa.kalmia.config.kalmiagram.client.bootstrap.ClientBootstrapConfig
import com.github.cao.awa.kalmia.constant.KalmiaConstant
import com.github.cao.awa.kalmia.env.KalmiaEnv
import com.github.cao.awa.kalmia.identity.PureExtraIdentity
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity
import com.github.cao.awa.kalmia.keypair.manager.KeypairManager
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore
import com.github.cao.awa.kalmia.message.Message
import com.github.cao.awa.kalmia.message.display.ClientMessage
import com.github.cao.awa.kalmia.message.manager.MessageManager
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo
import com.github.cao.awa.kalmia.network.packet.Packet
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter
import com.github.cao.awa.kalmia.session.manager.SessionManager
import com.github.cao.awa.kalmia.user.manager.UserManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.security.PrivateKey
import java.security.PublicKey
import java.util.function.BiConsumer
import java.util.function.Consumer

class KalmiaClient(config: ClientBootstrapConfig) {
    companion object {
        private val LOGGER: Logger = LogManager.getLogger("KalmiaClient")
        lateinit var clientBootstrapConfig: ClientBootstrapConfig

        @JvmStatic
        fun setupBootstrapConfig() {
            prepareConfig()

            clientBootstrapConfig = ClientBootstrapConfig.read(
                JSONObject.parse(IOUtil.read(FileReader(KalmiaConstant.CLIENT_CONFIG_PATH))),
                KalmiaEnv.DEFAULT_CLIENT_BOOTSTRAP_CONFIG
            )

            rewriteConfig(clientBootstrapConfig)
        }

        fun rewriteConfig(bootstrapConfig: ClientBootstrapConfig) {
            LOGGER.info("Rewriting client config")

            IOUtil.write(
                FileWriter(KalmiaConstant.CLIENT_CONFIG_PATH),
                bootstrapConfig.toJSON().toString(JSONWriter.Feature.PrettyFormat)
            )
        }

        fun prepareConfig() {
            LOGGER.info("Preparing client config")

            val configFile = File(KalmiaConstant.CLIENT_CONFIG_PATH)

            configFile.parentFile.mkdirs()

            if (!configFile.isFile) {
                IOUtil.write(
                    FileWriter(configFile),
                    IOUtil.read(InputStreamReader(ResourceLoader.stream(KalmiaConstant.CLIENT_DEFAULT_CONFIG_PATH)))
                )
            }
        }
    }

    private val bootstrapConfig: ClientBootstrapConfig
    val messageManager: MessageManager
    val userManager: UserManager
    val keypairManager: KeypairManager
    val sessionManager: SessionManager
    lateinit var activeCallback: Consumer<RequestRouter>
    private lateinit var router: RequestRouter

    init {
        try {
            bootstrapConfig = config

            messageManager = MessageManager("data/client/msg")
            userManager = UserManager("data/client/usr")
            keypairManager = KeypairManager("data/client/keypair")
            sessionManager = SessionManager("data/client/session")
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun router(router: RequestRouter) {
        this.router = router
    }

    fun activeCallback(activeCallback: Consumer<RequestRouter>): KalmiaClient {
        this.activeCallback = activeCallback
        return this
    }

    fun disconnect() {
        router.disconnect()
    }

    fun useEpoll(): Boolean {
        return bootstrapConfig.clientNetwork.useEpoll
    }

    fun connect() {
        KalmiaClientNetworkIo(this).connect(this.bootstrapConfig.clientNetwork)
    }

    fun sessionIds(): MutableSet<PureExtraIdentity> {
        return userManager.sessionListeners(router.accessIdentity())
    }

    fun curMsgSeq(sessionIdentity: PureExtraIdentity, awaitGet: Boolean): Long {
        if (awaitGet) {
            val receipt: ByteArray = Packet.createReceipt()

            return try {
                KalmiaEnv.awaitManager.awaitGet(receipt,
                    { messageManager.seq(sessionIdentity) },
                    { router.send(SelectMessagePacket(sessionIdentity, 0, 0).receipt(receipt)) },
                    true
                )
            } catch (e: Exception) {
                messageManager.seq(sessionIdentity)
            }
        } else {
            return messageManager.seq(sessionIdentity)
        }
    }

    fun getMessages(
        sessionIdentity: PureExtraIdentity, startSelect: Long, endSelect: Long, awaitGet: Boolean
    ): MutableList<Message> {
        val messages: MutableList<Message> = ApricotCollectionFactor.arrayList()

        if (awaitGet) {
            val receipt: ByteArray = Packet.createReceipt()

            try {
                KalmiaEnv.awaitManager.awaitGet(receipt, {
                    operationMessages({ _, msg -> messages.add(msg) }, sessionIdentity, startSelect, endSelect)
                    return@awaitGet null
                }, {
                    router.send(SelectMessagePacket(sessionIdentity, startSelect, endSelect).receipt(receipt))
                }, true)
            } catch (e: Exception) {
                messages.clear()

                operationMessages({ _, msg -> messages.add(msg) }, sessionIdentity, startSelect, endSelect)
            }
        } else {
            operationMessages({ _, msg -> messages.add(msg) }, sessionIdentity, startSelect, endSelect)
        }

        return messages
    }

    fun operationMessages(
        operator: BiConsumer<Long, Message>, sessionIdentity: PureExtraIdentity, startSelect: Long, endSelect: Long
    ) {
        messageManager.operation(sessionIdentity, startSelect, endSelect, operator)
    }

    fun getMessages(sessionIdentity: PureExtraIdentity, messageSeq: Long, awaitGet: Boolean): Message {
        return if (awaitGet) {
            val receipt: ByteArray = Packet.createReceipt()

            KalmiaEnv.awaitManager.awaitGet(receipt, { messageManager.get(sessionIdentity, messageSeq) }, {
                router.send(SelectMessagePacket(sessionIdentity, messageSeq, messageSeq).receipt(receipt))
            })
        } else {
            messageManager.get(sessionIdentity, messageSeq)
        }
    }

    fun getClientMessage(sessionIdentity: PureExtraIdentity, messageSeq: Long, awaitGet: Boolean): ClientMessage {
        val message: Message = getMessages(sessionIdentity, messageSeq, awaitGet)
        return ClientMessage(message.identity(), sessionIdentity, messageSeq, message.display())
    }

    fun getPublicKey(identity: PureExtraIdentity, awaitGet: Boolean): PublicKey {
        return if (awaitGet) {
            val receipt: ByteArray = Packet.createReceipt()

            KalmiaEnv.awaitManager.awaitGet(receipt,
                { keypairManager.publicKey(identity) },
                { router.send(SelectKeyStorePacket(identity).receipt(receipt)) })
        } else {
            keypairManager.publicKey(identity)
        }
    }

    fun getPrivateKey(identity: PureExtraIdentity, awaitGet: Boolean): PrivateKey? {
        return if (awaitGet) {
            val receipt: ByteArray = Packet.createReceipt()

            KalmiaEnv.awaitManager.awaitGet(receipt,
                { decryptPrivateKey(identity) },
                { router.send(SelectKeyStorePacket(identity).receipt(receipt)) })
        } else {
            decryptPrivateKey(identity)
        }
    }

    fun decryptPrivateKey(identity: PureExtraIdentity): PrivateKey? {
        return try {
            val store: KeyPairStore = keypairManager.getStore(identity)
            KeyStoreIdentity.createPrivateKey(
                store.type(), Crypto.aesDecrypt(
                    store.privateKey().key(),
                    // TODO
                    KalmiaEnv.testUer2AesCipher
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
