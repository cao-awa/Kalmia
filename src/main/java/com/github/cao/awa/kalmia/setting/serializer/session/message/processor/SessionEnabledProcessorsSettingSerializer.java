package com.github.cao.awa.kalmia.setting.serializer.session.message.processor;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.type.list.BytesListSerializer;
import com.github.cao.awa.kalmia.setting.key.session.message.processor.SessionEnabledProcessorsSetting;

import java.util.UUID;

@AutoBytesSerializer(value = 10001, target = SessionEnabledProcessorsSetting.class)
public class SessionEnabledProcessorsSettingSerializer implements BytesSerializer<SessionEnabledProcessorsSetting> {
    private static final BytesListSerializer<UUID> listSerializer = new BytesListSerializer<>();

    @Override
    public byte[] serialize(SessionEnabledProcessorsSetting target) {
        return listSerializer.serialize(target.processors());
    }

    @Override
    public SessionEnabledProcessorsSetting deserialize(BytesReader reader) {
        return new SessionEnabledProcessorsSetting(listSerializer.deserialize(reader));
    }
}
