package com.github.cao.awa.kalmia.framework.serialize.json.type.list;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoJsonSerializer;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializeFramework;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.json.JsonSerializer;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

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
        JSONObject ob = new JSONObject();

        JSONArray array = new JSONArray();

        for (Object o : target) {
            array.add(
                    KalmiaEnv.JSON_SERIALIZE_FRAMEWORK.getSerializer(o.getClass())
                                                      .getObject(EntrustEnvironment.cast(o))
            );
        }
        ob.put("type",
               target.get(0)
                     .getClass()
                     .getName()
        );
        ob.put(key,
               array
        );

        json.put(key,
                 ob
        );
    }

    // TODO
    // Not done yet the list deserialize.
    @Override
    public List<?> deserialize(JSONObject json, String key) {
        try {
            Class<?> type = Class.forName(json.getString("type"));

            JsonSerializer<?> serializer = KalmiaEnv.JSON_SERIALIZE_FRAMEWORK.getSerializer(type);

            List<?> list = ApricotCollectionFactor.arrayList();

            for (Object o : json.getJSONArray(key)) {
                JSONObject ob = EntrustEnvironment.cast(o);

                JSONObject delegate = new JSONObject();
                delegate.put("key",
                             ob
                );

                list.add(EntrustEnvironment.cast(serializer.deserialize(delegate,
                                                                        "key"
                )));
            }

            return list;
        } catch (Exception e) {

        }
        return null;
    }
}
