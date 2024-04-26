package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in;

import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.in.ChatInSessionEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request.RequestDuetSessionPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * This packet sender is server, response client request the session id for a target.
 * A sending case is in {@link RequestDuetSessionPacket}.
 *
 * @author cao_awa
 * @see RequestDuetSessionPacket
 * @since 1.0.0
 */
@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 16, crypto = true)
@NetworkEventTarget(ChatInSessionEvent.class)
public class ChatInSessionPacket extends Packet<AuthedRequestHandler> {
    private LongAndExtraIdentity targetUser;
    private PureExtraIdentity sessionIdentity;
}
