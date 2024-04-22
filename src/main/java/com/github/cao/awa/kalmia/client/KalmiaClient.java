package com.github.cao.awa.kalmia.client;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.annotations.config.AutoConfig;
import com.github.cao.awa.kalmia.config.client.KalmiaClientConfig;
import com.github.cao.awa.kalmia.config.client.bootstrap.network.KalmiaClientBootstrapNetworkConfig;
import com.github.cao.awa.kalmia.config.instance.ConfigEntry;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.keypair.KeyStoreIdentity;
import com.github.cao.awa.kalmia.keypair.manager.KeypairManager;
import com.github.cao.awa.kalmia.keypair.store.KeyPairStore;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.message.display.ClientMessage;
import com.github.cao.awa.kalmia.message.manager.MessageManager;
import com.github.cao.awa.kalmia.network.io.client.KalmiaClientNetworkIo;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.key.select.SelectKeyStorePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.message.select.SelectMessagePacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.session.manager.SessionManager;
import com.github.cao.awa.kalmia.user.manager.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class KalmiaClient {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaClient");
    @AutoConfig
    public final ConfigEntry<KalmiaClientConfig> clientBootstrapConfig = ConfigEntry.entry();
    @AutoConfig
    public final ConfigEntry<KalmiaClientBootstrapNetworkConfig> bootstrapConfig = ConfigEntry.entry();
    private final MessageManager messageManager;
    private final UserManager userManager;
    private final KeypairManager keypairManager;
    private final SessionManager sessionManager;
    private Consumer<RequestRouter> activeCallback;
    private RequestRouter router;

    public KalmiaClient() {
        try {
            this.messageManager = new MessageManager("data/client/msg");
            this.userManager = new UserManager("data/client/usr");
            this.keypairManager = new KeypairManager("data/client/keypair");
            this.sessionManager = new SessionManager("data/client/session");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public UserManager userManager() {
        return this.userManager;
    }

    public KeypairManager keypairManager() {
        return this.keypairManager;
    }

    public MessageManager messageManager() {
        return this.messageManager;
    }

    public SessionManager sessionManager() {
        return this.sessionManager;
    }

    public RequestRouter router() {
        return this.router;
    }

    public void router(RequestRouter router) {
        this.router = router;
    }

    public KalmiaClient activeCallback(Consumer<RequestRouter> activeCallback) {
        this.activeCallback = activeCallback;
        return this;
    }

    public Consumer<RequestRouter> activeCallback() {
        return this.activeCallback;
    }

    public void disconnect() {
        this.router.disconnect();
    }

    public boolean useEpoll() {
        return this.bootstrapConfig.get().useEpoll.get();
    }

    public void connect() throws Exception {
        new KalmiaClientNetworkIo(this).connect(this.bootstrapConfig.get());
    }

    public Set<PureExtraIdentity> sessionIds() {
        return userManager().sessionListeners(router().accessIdentity());
    }

    public long curMsgSeq(PureExtraIdentity sessionIdentity, boolean awaitGet) {
        if (awaitGet) {
            byte[] receipt = Packet.createReceipt();

            try {
                return KalmiaEnv.awaitManager.awaitGet(receipt,
                                                       () -> messageManager().seq(sessionIdentity),
                                                       () -> {
                                                           router().send(new SelectMessagePacket(sessionIdentity,
                                                                                                 0,
                                                                                                 0
                                                           ).receipt(receipt));
                                                       },
                                                       true
                );
            } catch (Exception e) {
                return messageManager().seq(sessionIdentity);
            }
        } else {
            return messageManager().seq(sessionIdentity);
        }
    }

    public List<Message> getMessages(PureExtraIdentity sessionIdentity, long startSelect, long endSelect, boolean awaitGet) {
        List<Message> messages = ApricotCollectionFactor.arrayList();

        if (awaitGet) {
            byte[] receipt = Packet.createReceipt();

            try {
                KalmiaEnv.awaitManager.awaitGet(receipt,
                                                () -> {
                                                    operationMessages((seq, msg) -> messages.add(msg),
                                                                      sessionIdentity,
                                                                      startSelect,
                                                                      endSelect
                                                    );

                                                    return null;
                                                },
                                                () -> {
                                                    router().send(new SelectMessagePacket(sessionIdentity,
                                                                                          startSelect,
                                                                                          endSelect
                                                    ).receipt(receipt));
                                                },
                                                true
                );
            } catch (Exception e) {
                messages.clear();

                operationMessages((seq, msg) -> messages.add(msg),
                                  sessionIdentity,
                                  startSelect,
                                  endSelect
                );
            }
        } else {
            operationMessages((seq, msg) -> messages.add(msg),
                              sessionIdentity,
                              startSelect,
                              endSelect
            );
        }

        return messages;
    }

    public void operationMessages(BiConsumer<Long, Message> operator, PureExtraIdentity sessionIdentity, long startSelect, long endSelect) {
        messageManager()
                .operation(sessionIdentity,
                           startSelect,
                           endSelect,
                           operator
                );
    }

    public Message getMessages(PureExtraIdentity sessionIdentity, long messageSeq, boolean awaitGet) {
        Message message = null;
        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                message = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> messageManager().get(sessionIdentity,
                                                   messageSeq
                        ),
                        () -> {
                            router().send(new SelectMessagePacket(sessionIdentity,
                                                                  messageSeq,
                                                                  messageSeq
                            ).receipt(receipt));
                        }
                );
            } else {
                message = messageManager().get(sessionIdentity,
                                               messageSeq
                );
            }
        } catch (InterruptedException ignored) {

        }

        return message;
    }

    public ClientMessage getClientMessage(PureExtraIdentity sessionIdentity, long messageSeq, boolean awaitGet) {
        Message message = getMessages(sessionIdentity,
                                      messageSeq,
                                      awaitGet
        );
        return ClientMessage.create(sessionIdentity,
                                    messageSeq,
                                    message
        );
    }

    public PublicKey getPublicKey(PureExtraIdentity identity, boolean awaitGet) {
        PublicKey publicKey = null;

        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                publicKey = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> keypairManager().publicKey(identity),
                        () -> router().send(new SelectKeyStorePacket(identity).receipt(receipt))
                );
            } else {
                publicKey = keypairManager().publicKey(identity);
            }
        } catch (InterruptedException ignored) {

        }

        return publicKey;
    }

    public PrivateKey getPrivateKey(PureExtraIdentity identity, boolean awaitGet) {
        PrivateKey publicKey = null;

        try {
            if (awaitGet) {
                byte[] receipt = Packet.createReceipt();

                publicKey = KalmiaEnv.awaitManager.awaitGet(
                        receipt,
                        () -> decryptPrivateKey(identity),
                        () -> router().send(new SelectKeyStorePacket(identity).receipt(receipt))
                );
            } else {
                publicKey = decryptPrivateKey(identity);
            }
        } catch (InterruptedException ignored) {

        }

        return publicKey;
    }

    public PrivateKey decryptPrivateKey(PureExtraIdentity identity) {
        try {
            KeyPairStore store = keypairManager().getStore(identity);
            return KeyStoreIdentity.createPrivateKey(
                    store.type(),
                    Crypto.aesDecrypt(store.privateKey()
                                           .key(),
                                      // TODO
                                      KalmiaEnv.testUer2AesCipher
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public Session getSession(PureExtraIdentity identity, boolean awaitGet) {
//
//    }
}
