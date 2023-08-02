package com.github.cao.awa.kalmia.event.network.inbound.message.notice;

import com.github.cao.awa.kalmia.event.network.NetworkEvent;
import com.github.cao.awa.kalmia.network.packet.inbound.message.notice.NewMessageNoticePacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;

public class NewMessageNoticeEvent extends NetworkEvent<NewMessageNoticePacket> {
    public NewMessageNoticeEvent(RequestRouter router, NewMessageNoticePacket packet) {
        super(router,
              packet
        );
    }
}
