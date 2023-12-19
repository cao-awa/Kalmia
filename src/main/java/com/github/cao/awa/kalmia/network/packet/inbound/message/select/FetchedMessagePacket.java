package com.github.cao.awa.kalmia.network.packet.inbound.message.select;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.actor.Getter;
import com.github.cao.awa.kalmia.annotations.auto.event.network.NetworkEventTarget;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotations.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotSet;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.select.FetchedMessageEvent;
import com.github.cao.awa.kalmia.identity.LongAndExtraIdentity;
import com.github.cao.awa.kalmia.message.Message;
import com.github.cao.awa.kalmia.network.handler.inbound.AuthedRequestHandler;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;

import java.util.List;
import java.util.Map;

@AutoSolvedPacket(id = 18, crypto = true)
@NetworkEventTarget(FetchedMessageEvent.class)
public class FetchedMessagePacket extends Packet<AuthedRequestHandler> {
    @AutoData
    @DoNotSet
    private List<Message> messages;

    @Client
    public FetchedMessagePacket(List<Message> messages) {
        this.messages = messages;
    }

    @Auto
    @Server
    public FetchedMessagePacket(BytesReader reader) {
        super(reader);
    }

    @Getter
    public List<Message> messages() {
        return this.messages;
    }

    public Map<LongAndExtraIdentity, Message> identified() {
        Map<LongAndExtraIdentity, Message> map = ApricotCollectionFactor.hashMap();
        messages().forEach(message -> map.put(message.identity(),
                                              message
        ));
        return map;
    }
}
