package com.github.cao.awa.kalmia.network.handler.handshake;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.router.UnsolvedRequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Set;

public class HandshakeHandler extends PacketHandler<HandshakeHandler> {
    private static final Set<RequestStatus> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(),
                                                                                        set -> {
                                                                                            set.add(RequestStatus.HELLO);
                                                                                        }
    );
    private byte[] rsaPrikey = BytesUtil.EMPTY;
    private byte[] rsaPubkey = BytesUtil.EMPTY;

    public byte[] getRsaPrikey() {
        return this.rsaPrikey;
    }

    public byte[] getRsaPubkey() {
        return this.rsaPubkey;
    }

    public HandshakeHandler() {

    }

    public void setupRsa() {
        try {
            KeyPair keyPair = Crypto.rsaKeypair(4096);
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            this.rsaPrikey = privateKey.getEncoded();
            this.rsaPubkey = publicKey.getEncoded();
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inbound(ReadonlyPacket<HandshakeHandler> packet, UnsolvedRequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestStatus> allowStatus() {
        return ALLOW_STATUS;
    }
}
