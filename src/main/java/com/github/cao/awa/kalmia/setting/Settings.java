package com.github.cao.awa.kalmia.setting;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.serialize.bytes.type.map.BytesMapSerializer;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;

import java.util.Map;

public class Settings {
    private static final BytesMapSerializer<String, Setting> mapSerializer = new BytesMapSerializer<>();
    private final Map<String, Setting> settings = ApricotCollectionFactor.hashMap();

    public <T extends Setting> T get(T setting) {
        Setting settingValue = this.settings.get(setting.settingKey());
        if (settingValue == null) {
            return setting;
        }
        return Manipulate.cast(settingValue);
    }

    public <T extends Setting> Settings set(T setting) {
        this.settings.put(setting.settingKey(),
                          setting
        );
        return this;
    }

    public static Settings create(BytesReader reader) {
        Settings settings = new Settings();

        mapSerializer.deserialize(reader)
                     .forEach((key, value) -> {
                         settings.set(Manipulate.cast(value));
                     });

        return settings;
    }

    public byte[] toBytes() {
        return mapSerializer.serialize(this.settings);
    }
}
