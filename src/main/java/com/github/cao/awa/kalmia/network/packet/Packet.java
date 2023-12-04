package com.github.cao.awa.kalmia.network.packet;

import com.github.cao.awa.apricot.annotations.auto.Auto;
import com.github.cao.awa.apricot.identifier.BytesRandomIdentifier;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.apricot.util.time.TimeUtil;
import com.github.cao.awa.kalmia.annotations.inaction.DoNotOverride;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * @param <T>
 * @see UnsolvedPacket
 */
public abstract class Packet<T extends PacketHandler<T>> {
    private static final Logger LOGGER = LogManager.getLogger("Packet");
    public static final byte[] RECEIPT = new byte[]{- 1};
    private byte[] receipt = RECEIPT;
    private T handler;

    public Packet(byte[] receipt) {
        this.receipt = checkReceipt(receipt);
    }

    public Packet(BytesReader reader) {
        try {
            KalmiaEnv.packetFramework.create(this,
                                             reader
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Packet() {

    }

    @DoNotOverride
    public final byte[] receipt() {
        return this.receipt;
    }

    @DoNotOverride
    public final byte[] encodeReceipt() {
        if (this.receipt == RECEIPT) {
            return this.receipt;
        }
        return BytesUtil.concat(new byte[]{1},
                                this.receipt
        );
    }

    public static byte[] checkReceipt(byte[] receipt) {
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

    public static byte[] readReceipt(BytesReader reader) {
        return reader.read(16);
    }

    public static byte[] createReceipt() {
        return BytesRandomIdentifier.create(16);
    }

    @Auto
    @DoNotOverride
    public byte[] payload() {
        return EntrustEnvironment.trys(
                // Encode payload.
                () -> KalmiaEnv.packetFramework.payload(this),
                // Handle exception.
                e -> {
                    // Usually, exception should not be happened, maybe bugs cause this.
                    // Need report to solve the bug.
                    LOGGER.error("Unexpected exception happened when encoding payload, please report this",
                                 e
                    );
                }
        );
    }

    @Auto
    public byte[] id() {
        // Encode id.
        return KalmiaEnv.packetFramework.id(this);
    }

    @Auto
    @DoNotOverride
    public final byte[] encode(RequestRouter router) {
        byte[] payload = BytesUtil.concat(
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
        );

        byte[] digest = MessageDigger.digestToBytes(payload,
                                                    MessageDigger.Sha3.SHA_256
        );

        return router.encode(BytesUtil.concat(
                new byte[]{(byte) digest.length},
                digest,
                payload
        ));
    }

    @Auto
    @DoNotOverride
    public void inbound(RequestRouter router, T handler) {
        this.handler = handler;
        try {
            KalmiaEnv.networkEventFramework.fireEvent(router,
                                                      handler,
                                                      this
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Auto
    @DoNotOverride
    public final T handler() {
        return this.handler;
    }

    @DoNotOverride
    public final <X extends Packet<T>> X receipt(byte[] receipt) {
        this.receipt = checkReceipt(receipt);
        return EntrustEnvironment.cast(this);
    }

    @DoNotOverride
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
