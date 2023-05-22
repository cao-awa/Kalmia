package com.github.cao.awa.kalmia.framework.serialize.type.map;

import com.github.cao.awa.apricot.annotation.Unsupported;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Map;

@Unsupported
public class MapSerializer implements BytesSerializer<Map<?, ?>> {
    @Override
    public byte[] serialize(Map<?, ?> objects) {
        return null;
    }

    @Override
    public Map<?, ?> deserialize(BytesReader element) {
        return null;
    }

    @Override
    public Map<?, ?> initializer() {
        return ApricotCollectionFactor.newHashMap();
    }

    @Override
    public Class<Map<?, ?>>[] target() {
        return EntrustEnvironment.cast(Map.class);
    }

    @Override
    public long id() {
        return 501;
    }
}
