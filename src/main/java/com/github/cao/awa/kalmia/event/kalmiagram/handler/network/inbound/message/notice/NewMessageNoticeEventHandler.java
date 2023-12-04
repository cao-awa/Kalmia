package com.github.cao.awa.kalmia.event.kalmiagram.handler.network.inbound.message.notice;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.kalmia.annotations.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.handler.network.NetworkEventHandler;
import com.github.cao.awa.kalmia.event.kalmiagram.network.inbound.message.notice.NewMessageNoticeEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

@Auto
@Client
@AutoHandler(NewMessageNoticeEvent.class)
public interface NewMessageNoticeEventHandler extends NetworkEventHandler<NewMessageNoticePacket, NewMessageNoticeEvent> {
}
