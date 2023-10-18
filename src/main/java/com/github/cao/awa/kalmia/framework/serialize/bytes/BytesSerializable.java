package com.github.cao.awa.kalmia.framework.serialize.bytes;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;

public interface BytesSerializable<T> {
    @Auto
    byte[] serialize();

    @Auto
    T deserialize(BytesReader reader);
}
