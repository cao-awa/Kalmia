package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public abstract class Packet<T extends PacketHandler<T>> {
    private static final Logger LOGGER = LogManager.getLogger("Packet");
    public static final byte[] RECEIPT = new byte[]{- 1};
    private byte[] receipt = RECEIPT;

    public Packet(byte[] receipt) {
        this.receipt = check(receipt);
    }

    public Packet(BytesReader reader) {
        try {
            KalmiaEnv.packetSerializeFramework.create(this,
                                                      reader
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Packet() {

    }

    public byte[] receipt() {
        return this.receipt;
    }

    public byte[] encodeReceipt() {
        if (this.receipt == RECEIPT) {
            return this.receipt;
        }
        return BytesUtil.concat(new byte[]{1},
                                this.receipt
        );
    }

    public static byte[] check(byte[] receipt) {
        if (Arrays.equals(RECEIPT,
                          receipt
        )) {
            return RECEIPT;
        }
        if (receipt.length != 16) {
            throw new IllegalArgumentException("Receipt data only allowed 16 bytes");
        }
        return receipt;
    }

    public static byte[] read(BytesReader reader) {
        return reader.read(16);
    }

    @Auto
    public byte[] payload() {
        return EntrustEnvironment.trys(
                // Encode payload
                () -> KalmiaEnv.packetSerializeFramework.payload(this),
                // Handle exception
                e -> {
                    // Usually, exception should not be happened, maybe bugs cause this.
                    // Need report to solve the bug.
                    LOGGER.error("Unexpected exception happened when encoding payload, please report this",
                                 e
                    );
                }
        );
    }

    public byte[] id() {
        // Encode id.
        return KalmiaEnv.packetSerializeFramework.id(this);
    }

    public byte[] encode(RequestRouter router) {
        return router.encode(BytesUtil.concat(
                // Unequal random identifier and timestamp for every packet.
                // For protect the replay attack.
                BytesRandomIdentifier.create(16),
                SkippedBase256.longToBuf(TimeUtil.nano()),
                // Packet id, used in deserialize.
                id(),
                // Packet receipt, used to reply request.
                encodeReceipt(),
                // The packet payload here.
                payload()
        ));
    }

    public abstract void inbound(RequestRouter router, T handler);

    public <X extends Packet<T>> X receipt(byte[] receipt) {
        this.receipt = check(receipt);
        return EntrustEnvironment.cast(this);
    }

    public int size() {
        return
                // 24 is a random identifier(16) add timestamp(8)
                24 +
                        // Id length
                        id().length +
                        // Receipt length, 16 or 1
                        receipt().length +
                        // Payload length
                        payload().length;
    }
}
