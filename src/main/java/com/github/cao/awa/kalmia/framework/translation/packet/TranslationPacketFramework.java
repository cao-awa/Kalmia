package com.github.cao.awa.kalmia.framework.translation.packet;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.translation.Translation;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.trtr.util.string.StringConcat;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.lang.reflect.Constructor;
import java.util.Map;

public class TranslationPacketFramework extends ReflectionFramework {
    private final Map<String, Map<String, Constructor<? extends TranslationPacket>>> constructors = ApricotCollectionFactor.hashMap();

    @Override
    public void work() {
        reflection().getTypesAnnotatedWith(Translation.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(Translation.class) && TranslationPacket.class.isAssignableFrom(clazz);
    }

    public Class<? extends TranslationPacket> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends TranslationPacket> packet) {
        Translation translationAnnotation = packet.getAnnotation(Translation.class);

        String type = translationAnnotation.type();
        String name = translationAnnotation.name();

        Constructor<? extends TranslationPacket> constructor = EntrustEnvironment.trys(() -> EntrustEnvironment.cast(ensureAccessible(packet.getConstructor(JSONObject.class))),
                                                                                       ex -> {
                                                                                           return EntrustEnvironment.trys(() -> ensureAccessible(packet.getConstructor()),
                                                                                                                          ex0 -> {
                                                                                                                              BugTrace.trace(ex0,
                                                                                                                                             StringConcat.concat(
                                                                                                                                                     "The packet '",
                                                                                                                                                     packet.getName(),
                                                                                                                                                     "' are missing the standard constructor, but it using @Translation annotation to invert control by type '",
                                                                                                                                                     type,
                                                                                                                                                     "' and name '",
                                                                                                                                                     name,
                                                                                                                                                     "'"
                                                                                                                                             ),
                                                                                                                                             true
                                                                                                                              );
                                                                                                                              return null;
                                                                                                                          }
                                                                                           );
                                                                                       }
        );

        this.constructors.compute(type,
                                  (key, value) -> {
                                      if (value == null) {
                                          value = ApricotCollectionFactor.hashMap();
                                      }
                                      value.put(name,
                                                constructor
                                      );
                                      return value;
                                  }
        );
    }

    public TranslationPacket createPacket(JSONObject json) throws Exception {
        String type = json.getString("post_type");
        String name = json.getString("post_name");

        return this.constructors.get(type)
                                .get(name)
                                .newInstance(json.getJSONObject("data"));
    }
}
