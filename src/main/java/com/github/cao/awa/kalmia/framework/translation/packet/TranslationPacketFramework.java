package com.github.cao.awa.kalmia.framework.translation.packet;

import com.alibaba.fastjson2.JSONObject;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.catheter.Catheter;
import com.github.cao.awa.kalmia.annotations.translation.Translation;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.translation.network.packet.TranslationPacket;
import com.github.cao.awa.sinuatum.manipulate.Manipulate;
import com.github.cao.awa.trtr.util.string.StringConcat;

import java.lang.reflect.Constructor;
import java.util.Map;

public class TranslationPacketFramework extends ReflectionFramework {
    private final Map<String, Map<String, Constructor<? extends TranslationPacket>>> constructors = ApricotCollectionFactor.hashMap();

    @Override
    public void work() {
        Catheter.of(reflection().getTypesAnnotatedWith(Translation.class))
                .filter(this :: match)
                .vary(this :: cast)
                .each(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(Translation.class) && TranslationPacket.class.isAssignableFrom(clazz);
    }

    public Class<? extends TranslationPacket> cast(Class<?> clazz) {
        return Manipulate.cast(clazz);
    }

    public void build(Class<? extends TranslationPacket> packet) {
        Translation translationAnnotation = packet.getAnnotation(Translation.class);

        String type = translationAnnotation.type();
        String name = translationAnnotation.name();

        Constructor<? extends TranslationPacket> constructor = Manipulate
                .supply(
                        () -> ensureAccessible(packet.getConstructor(JSONObject.class))
                )
                .get(
                        ex -> Manipulate.cast(
                                Manipulate.supply(() -> ensureAccessible(packet.getConstructor()))
                                          .get(
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
                                          )
                        )
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
                                .newInstance(json.getJSONObject("data"))
                                .clientTimestamp(json.getLong("time"))
                                .clientIdentity(json.getString("identity"));
    }
}
