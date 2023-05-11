package com.github.cao.awa.kalmia.framework.serialize;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.framework.serialize.serializer.BytesSerializer;
import com.github.cao.awa.kalmia.framework.serialize.type.array.ByteArraySerializer;
import com.github.cao.awa.kalmia.framework.serialize.type.raw.*;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public class ByteSerializeFramework extends ReflectionFramework {
    private final Map<Class<?>, BytesSerializer<?>> typeToSerializers = ApricotCollectionFactor.newHashMap();
    private final Map<Long, BytesSerializer<?>> idToSerializers = ApricotCollectionFactor.newHashMap();

    private void initNbtSerializers() {
        registerSerializer(new BooleanSerializer(),
                           boolean.class,
                           Boolean.class
        );
        registerSerializer(new ByteSerializer(),
                           byte.class,
                           Byte.class
        );
        registerSerializer(new CharSerializer(),
                           char.class,
                           Character.class
        );
        registerSerializer(new ShortSerializer(),
                           short.class,
                           Short.class
        );
        registerSerializer(new IntSerializer(),
                           int.class,
                           Integer.class
        );
        registerSerializer(new LongSerializer(),
                           long.class,
                           Long.class
        );
        registerSerializer(new BigIntegerSerializer(),
                           BigInteger.class
        );
        registerSerializer(new FloatSerializer(),
                           float.class,
                           Float.class
        );
        registerSerializer(new DoubleSerializer(),
                           double.class,
                           Double.class
        );
        registerSerializer(new BigDecimalSerializer(),
                           BigDecimal.class
        );
        registerSerializer(new StringSerializer(),
                           String.class
        );
//        registerSerializer(new NbtListSerializer(),
//                           List.class
//        );

        registerSerializer(new ByteArraySerializer(),
                           byte[].class
        );
    }

    public final <T> void registerSerializer(BytesSerializer<T> serializer, @Nullable Class<?>... matchType) {
        if (matchType == null) {
            return;
        }
        for (Class<?> t : matchType) {
            this.typeToSerializers.put(t,
                                       serializer
            );
        }
        this.idToSerializers.put(serializer.id(),
                                 serializer
        );
    }

    public <T> BytesSerializer<T> getSerializer(Class<T> type) {
        if (type == null) {
            return null;
        }
        BytesSerializer<T> serializer = EntrustEnvironment.cast(this.typeToSerializers.get(type));
        if (serializer == null) {
            serializer = EntrustEnvironment.cast(getSerializer(type.getSuperclass()));
            if (serializer == null) {
                for (Class<?> aInterface : type.getInterfaces()) {
                    serializer = EntrustEnvironment.cast(getSerializer(aInterface));
                    if (serializer != null) {
                        break;
                    }
                }
            }
        }
        return serializer;
    }

    public <T> BytesSerializer<T> getSerializer(long id) {
        return EntrustEnvironment.cast(this.idToSerializers.get(id));
    }

    @Override
    public void work() {
        initNbtSerializers();

    }
}