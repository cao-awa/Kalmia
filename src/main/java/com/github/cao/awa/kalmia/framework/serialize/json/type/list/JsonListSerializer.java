package com.github.cao.awa.kalmia.framework.serialize.json.type.list;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializeFramework;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;

import java.util.List;

/**
 * The serializer used to {@link List} in {@link BytesSerializeFramework}.<br>
 *
 * @author cao_awa
 * @author 草二号机
 * @see BytesSerializer
 * @see BytesSerializeFramework
 * @see List
 * @since 1.0.0
 */
@AutoJsonSerializer(target = List.class)
public class JsonListSerializer implements JsonSerializer<List<?>> {
    @Override
    public void serialize(JSONObject json, String key, List<?> target) {
        json.put(key,
                 target
        );
    }

    // TODO
    // Not done yet the list deserialize.
    @Override
    public List<?> deserialize(JSONObject json, String key) {
        return null;
    }
}
