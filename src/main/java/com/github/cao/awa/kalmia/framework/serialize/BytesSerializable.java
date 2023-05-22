package com.github.cao.awa.kalmia.framework.serialize;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;

public interface BytesSerializable {
    @Auto
    byte[] serialize();

    @Auto
    void deserialize(BytesReader reader);
}
