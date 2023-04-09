package com.github.cao.awa.kalmia.network.encode.crypto.symmetric.no;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.network.encode.crypto.symmetric.SymmetricCrypto;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.util.Arrays;

public class NoCrypto extends SymmetricCrypto {
    public NoCrypto() {
        super(BytesUtil.EMPTY);
    }

    @Override
    public byte[] encode(byte[] plains) throws Exception {
        return BytesUtil.concat(new byte[]{- 2},
                                plains
        );
    }

    @Override
    public byte[] decode(byte[] ciphertext) throws Exception {
        BytesReader reader = new BytesReader(ciphertext);
        if (reader.read() == - 2) {
            return reader.all();
        }
        return null;
    }
}
