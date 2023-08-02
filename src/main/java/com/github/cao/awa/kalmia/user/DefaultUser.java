package com.github.cao.awa.kalmia.user;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.user.key.ServerKeyPairStore;
import com.github.cao.awa.kalmia.user.password.UserPassword;
import com.github.cao.awa.kalmia.user.pubkey.PublicKeyIdentity;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

public class DefaultUser extends User {
    private final long joinTimestamp;
    private final UserPassword password;
    private final ServerKeyPairStore keyPair;

    public UserPassword password() {
        return this.password;
    }

    public DefaultUser(long timestamp, String password, ServerKeyPairStore keyPair) {
        this.joinTimestamp = timestamp;
        this.password = new UserPassword(password.getBytes(StandardCharsets.UTF_8));
        this.keyPair = keyPair;
    }

    public DefaultUser(long timestamp, byte[] password, ServerKeyPairStore keyPair) {
        this.joinTimestamp = timestamp;
        this.password = new UserPassword(password);
        this.keyPair = keyPair;
    }

    public DefaultUser(long timestamp, UserPassword password, ServerKeyPairStore keyPair) {
        this.joinTimestamp = timestamp;
        this.password = password;
        this.keyPair = keyPair;
    }

    public long joinTimestamp() {
        return this.joinTimestamp;
    }

    public static DefaultUser create(BytesReader reader) {
        if (reader.read() == 0) {
            long timestamp = SkippedBase256.readLong(reader);
            UserPassword password = UserPassword.create(reader);

            return new DefaultUser(timestamp,
                                   password,
                                   ServerKeyPairStore.create(reader)
            );
        } else {
            return null;
        }
    }

    public ServerKeyPairStore keyPair() {
        return this.keyPair;
    }

    public PublicKey publicKey() {
        return EntrustEnvironment.trys(() -> PublicKeyIdentity.getKey(this.keyPair));
    }

    @Override
    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{0},
                                SkippedBase256.longToBuf(this.joinTimestamp),
                                this.password.toBytes(),
                                this.keyPair.toBytes()
        );
    }
}
