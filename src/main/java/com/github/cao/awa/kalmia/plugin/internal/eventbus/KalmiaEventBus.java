package com.github.cao.awa.kalmia.plugin.internal.eventbus;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.annotations.auto.AutoPlugin;
import com.github.cao.awa.kalmia.plugin.Plugin;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.in.ChatInSessionEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.listeners.SessionListenersUpdateEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.request.RequestDuetSessionUpdateEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.chat.session.request.RequestGroupSessionEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.disconnect.TryDisconnectEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.crypto.aes.HandshakeAesCipherEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.crypto.ec.HandshakePreSharedEcEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.hello.ClientHelloEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.handshake.hello.ServerHelloEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.invalid.operation.DeleteMessageEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.key.select.SelectKeyStoreEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.key.select.SelectedKeyStoreEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.feedback.LoginFailureEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.feedback.LoginSuccessEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.password.LoginWithPasswordEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.sign.LoginWithSignEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.login.token.LoginWithTokenEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.delete.DeletedMessageEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.notice.NewMessageNoticeEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.select.SelectMessageEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.select.SelectedMessageEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send.SendMessageEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send.SendMessageRefusedEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.message.send.SentMessageEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.ping.TryPingEventBus;
import com.github.cao.awa.kalmia.plugin.internal.eventbus.handler.ping.TryPingResponseEventBus;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Auto
@AutoPlugin(
        name = "kalmia_eventbus",
        uuid = "C942B874-2E65-CCB4-8B8C-0C743E7BE818"
)
public class KalmiaEventBus extends Plugin {
    private static final Logger LOGGER = LogManager.getLogger("KalmiaTranslation");
    @Client
    public static final ChatInSessionEventBus chatInSession = new ChatInSessionEventBus();
    @Client
    public static final SessionListenersUpdateEventBus sessionListenersUpdate = new SessionListenersUpdateEventBus();
    @Server
    public static final RequestDuetSessionUpdateEventBus requestDuetSession = new RequestDuetSessionUpdateEventBus();
    @Server
    public static final RequestGroupSessionEventBus requestGroupSession = new RequestGroupSessionEventBus();
    public static final TryDisconnectEventBus tryDisconnect = new TryDisconnectEventBus();
    @Server
    public static final HandshakeAesCipherEventBus handshakeAesCipher = new HandshakeAesCipherEventBus();
    @Client
    public static final HandshakePreSharedEcEventBus handshakePreSharedEc = new HandshakePreSharedEcEventBus();
    @Server
    public static final ClientHelloEventBus clientHello = new ClientHelloEventBus();
    @Client
    public static final ServerHelloEventBus serverHello = new ServerHelloEventBus();
    @Client
    public static final DeleteMessageEventBus operationInvalid = new DeleteMessageEventBus();
    @Server
    public static final SelectKeyStoreEventBus selectKeyStore = new SelectKeyStoreEventBus();
    @Client
    public static final SelectedKeyStoreEventBus selectedKeyStore = new SelectedKeyStoreEventBus();
    @Client
    public static final LoginFailureEventBus loginFailure = new LoginFailureEventBus();
    @Client
    public static final LoginSuccessEventBus loginSuccess = new LoginSuccessEventBus();
    @Server
    public static final LoginWithPasswordEventBus loginWithPassword = new LoginWithPasswordEventBus();
    @Server
    public static final LoginWithSignEventBus loginWithSign = new LoginWithSignEventBus();
    @Server
    public static final LoginWithTokenEventBus loginWithToken = new LoginWithTokenEventBus();
    @Server
    public static final DeleteMessageEventBus deleteMessage = new DeleteMessageEventBus();
    @Client
    public static final DeletedMessageEventBus deletedMessage = new DeletedMessageEventBus();
    @Client
    public static final NewMessageNoticeEventBus newMessageNotice = new NewMessageNoticeEventBus();
    @Server
    public static final SelectMessageEventBus selectMessage = new SelectMessageEventBus();
    @Client
    public static final SelectedMessageEventBus selectedMessage = new SelectedMessageEventBus();
    @Server
    public static final SendMessageEventBus sendMessage = new SendMessageEventBus();
    @Client
    public static final SentMessageEventBus sentMessage = new SentMessageEventBus();
    @Client
    public static final SendMessageRefusedEventBus sendMessageRefused = new SendMessageRefusedEventBus();
    @Server
    public static final TryPingEventBus tryPing = new TryPingEventBus();
    @Client
    public static final TryPingResponseEventBus tryPingResponse = new TryPingResponseEventBus();

    @Override
    public void onLoad() {
        LOGGER.info("Loading kalmia eventbus");
        registerHandlers(
                chatInSession,
                sessionListenersUpdate,
                requestDuetSession,
                requestGroupSession,
                tryDisconnect,
                handshakeAesCipher,
                handshakePreSharedEc,
                operationInvalid,
                selectKeyStore,
                selectedKeyStore,
                loginFailure,
                loginSuccess,
                clientHello,
                serverHello,
                loginWithPassword,
                loginWithSign,
                loginWithToken,
                deleteMessage,
                deletedMessage,
                newMessageNotice,
                selectMessage,
                selectedMessage,
                sendMessage,
                sentMessage,
                sendMessageRefused,
                tryPing,
                tryPingResponse
        );
    }
}
