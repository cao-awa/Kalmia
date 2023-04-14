package com.github.cao.awa.kalmia.network.packet.request.ping.unstatus;

import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.WritablePacket;
import com.github.cao.awa.kalmia.network.packet.inbound.ping.TryPingPacket;
import com.github.cao.awa.modmdo.annotation.platform.Client;

/**
 * @see TryPingPacket
 */
@Client
public class TryPingRequest extends WritablePacket {
    public static final byte[] ID = SkippedBase256.longToBuf(- 1);

    @Override
    public byte[] data() {
        return SkippedBase256.longToBuf(TimeUtil.nano());
    }

    @Override
    public byte[] id() {
        return ID;
    }
}
