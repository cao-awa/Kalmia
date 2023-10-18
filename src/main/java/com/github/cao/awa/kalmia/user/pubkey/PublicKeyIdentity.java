package com.github.cao.awa.kalmia.user.pubkey;

import com.github.cao.awa.apricot.util.encryption.Crypto;
import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.mathematic.base.Base256;
import com.github.cao.awa.kalmia.network.packet.inbound.setting.NotDoneSettingsPacket;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.user.key.ServerKeyPairStore;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;

public class PublicKeyIdentity {
    public static final int RSA_IDENTITY = 0;
    public static final int EC_IDENTITY = 1;

    public static PublicKey createKey(int identity, byte[] data) {
        return switch (identity) {
            case RSA_IDENTITY -> Crypto.decodeRsaPubkey(data);
            case EC_IDENTITY -> Crypto.decodeEcPubkey(data);
            default -> throw new IllegalStateException("Unexpected value: " + identity);
        };
    }

    public static byte[] encodeKey(PublicKey publicKey) {
        return BytesUtil.concat(
                Base256.tagToBuf(getIdentity(publicKey)),
                publicKey.getEncoded()
        );
    }

    public static byte getIdentity(PublicKey publicKey) {
        if (publicKey instanceof RSAPublicKey) {
            return RSA_IDENTITY;
        } else if (publicKey instanceof ECPublicKey) {
            return EC_IDENTITY;
        }
        return - 1;
    }

    public static PublicKey getKey(ServerKeyPairStore store) {
        return createKey(store.type(),
                         store.publicKey()
        );
    }

    public static boolean ensurePublicKeySettings(long uid, RequestRouter router) {
        if (Kalmia.SERVER.userManager()
                         .publicKey(uid) == null) {
            router.send(new NotDoneSettingsPacket("settings.public_key"));
            return false;
        }
        return true;
    }
}
