package com.github.cao.awa.kalmia.setting.serializer.lang;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.setting.key.lang.LanguageSetting;
import com.github.cao.awa.viburnum.util.bytes.BytesUtil;

import java.nio.charset.StandardCharsets;

@AutoBytesSerializer(value = 10000, target = LanguageSetting.class)
public class LanguageSettingSerializer implements BytesSerializer<LanguageSetting> {
    @Override
    public byte[] serialize(LanguageSetting target) {
        return BytesUtil.concat(
                new byte[]{(byte) target.languageKey()
                                        .length()},
                target.languageKey()
                      .getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public LanguageSetting deserialize(BytesReader reader) {
        return new LanguageSetting(new String(reader.read(reader.read()),
                                              StandardCharsets.UTF_8
        ));
    }
}
