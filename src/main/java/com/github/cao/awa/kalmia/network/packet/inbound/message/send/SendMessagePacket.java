package com.github.cao.awa.kalmia.network.packet.inbound.message.send;

import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoAllData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.constant.KalmiaConstant;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.send.SendMessageEvent;
import com.github.cao.awa.kalmia.identity.PureExtraIdentity;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter
@AutoAllData
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@AutoSolvedPacket(id = 100, crypto = true)
@NetworkEventTarget(SendMessageEvent.class)
public class SendMessagePacket extends Packet<AuthedRequestHandler> {
    private PureExtraIdentity sessionIdentity;
    private PureExtraIdentity keyIdentity;
    private PureExtraIdentity signIdentity;
    private byte[] message;
    private byte[] sign;
    private boolean disableProcessor;

    public boolean signed() {
        return ! this.signIdentity.equals(KalmiaConstant.UNMARKED_PURE_IDENTITY);
    }

    public boolean crypted() {
        return ! this.keyIdentity.equals(KalmiaConstant.UNMARKED_PURE_IDENTITY);
    }
}
