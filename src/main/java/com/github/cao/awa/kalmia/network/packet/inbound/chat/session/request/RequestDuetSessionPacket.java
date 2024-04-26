package com.github.cao.awa.kalmia.network.packet.inbound.chat.session.request;

import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.chat.session.request.RequestDuetSessionEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.inbound.chat.session.in.ChatInSessionPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * This packet sender is client, use to request the session id for a target. <br>
 * The response of this packet is {@link ChatInSessionPacket}.
 *
 * @author cao_awa
 * @see ChatInSessionPacket
 * @since 1.0.0
 */
@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 50, crypto = true)
@NetworkEventTarget(RequestDuetSessionEvent.class)
public class RequestDuetSessionPacket extends Packet<AuthedRequestHandler> {
    private LongAndExtraIdentity targetUser;
}
