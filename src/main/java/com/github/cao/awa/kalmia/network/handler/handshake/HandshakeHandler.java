package com.github.cao.awa.kalmia.network.handler.handshake;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.network.handler.PacketHandler;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.router.RequestRouter;
import com.github.cao.awa.kalmia.network.router.status.RequestStatus;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;

public class HandshakeHandler extends PacketHandler<HandshakeHandler> {
    private static final Set<RequestStatus> ALLOW_STATUS = EntrustEnvironment.operation(ApricotCollectionFactor.newHashSet(),
                                                                                        set -> {
                                                                                            set.add(RequestStatus.HELLO);
                                                                                        }
    );
    private RSAPublicKey rsaPubkey;
    private RSAPrivateKey rsaPrikey;

    public byte[] getRsaPrikeyData() {
        return this.rsaPrikey.getEncoded();
    }

    public byte[] getRsaPubkeyData() {
        return this.rsaPubkey.getEncoded();
    }

    public RSAPublicKey getRsaPubkey() {
        return rsaPubkey;
    }

    public RSAPrivateKey getRsaPrikey() {
        return rsaPrikey;
    }

    public HandshakeHandler() {

    }

    public void setupRsa(String cipherKey) {
        try {
            this.rsaPrikey = KalmiaEnv.DEFAULT_PRE_PRIKEY.get(cipherKey);
        } catch (Exception e) {
            // TODO
            throw new RuntimeException(e);
        }
    }

    @Override
    public void inbound(ReadonlyPacket<HandshakeHandler> packet, RequestRouter router) {
        packet.inbound(router,
                       this
        );
    }

    @Override
    public Set<RequestStatus> allowStatus() {
        return ALLOW_STATUS;
    }
}
