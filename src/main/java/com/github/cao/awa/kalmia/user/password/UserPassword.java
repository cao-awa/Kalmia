package com.github.cao.awa.kalmia.user.password;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.digger.MessageDigger;
import com.github.cao.awa.kalmia.convert.BytesValueConvertable;
import com.github.cao.awa.kalmia.mathematic.Mathematics;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

public class UserPassword extends BytesValueConvertable {
    private final boolean isSha;
    private final byte[] password;

    public UserPassword(boolean isSha, byte[] password) {
        this.isSha = isSha;
        this.password = password;
    }

    public UserPassword(byte[] password) {
        this.isSha = true;
        this.password = Mathematics.toBytes(MessageDigger.digest(password,
                                                                 MessageDigger.Sha3.SHA_512
                                            ),
                                            16
        );
    }

    public boolean isSha() {
        return this.isSha;
    }

    public byte[] password() {
        return this.password;
    }

    public byte[] toBytes() {
        return BytesUtil.concat(new byte[]{(byte) (this.isSha ? 0 : - 1)},
                                new byte[]{(byte) this.password.length},
                                this.password
        );
    }

    public static UserPassword create(BytesReader reader) {
        boolean isSha = reader.read() != -1;

        int length = reader.read();
        byte[] password = reader.read(length);

        return new UserPassword(isSha, password);
    }

    @Override
    public byte[] value() {
        return password();
    }
}
