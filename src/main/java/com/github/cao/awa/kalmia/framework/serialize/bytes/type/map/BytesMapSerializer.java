package com.github.cao.awa.kalmia.framework.serialize.bytes.type.map;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotations.auto.serializer.AutoBytesSerializer;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializable;
import com.github.cao.awa.kalmia.framework.serialize.bytes.BytesSerializer;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@AutoBytesSerializer(value = 501, target = Map.class)
public class BytesMapSerializer<K, V> implements BytesSerializer<Map<K, V>> {
    private static final byte[] EMPTY = new byte[]{- 1};

    @Override
    public byte[] serialize(Map<K, V> map) {
        if (map.isEmpty()) {
            return EMPTY;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                write(output,
                      entry
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return output.toByteArray();
    }

    private static void write(ByteArrayOutputStream output, Map.Entry<?, ?> entry) throws IOException {
        write(output,
              entry.getKey()
        );

        write(output,
              entry.getValue()
        );
    }

    public static void write(ByteArrayOutputStream output, Object o) throws IOException {
        BytesSerializer<Object> keySerializer = KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.getSerializer(o);

        if (keySerializer != null) {
            // Write key by id mode.
            output.write(1);
            output.write(SkippedBase256.longToBuf(keySerializer.id()));
            output.write(keySerializer.serialize(o));
        } else {
            if (o instanceof BytesSerializable<?> serializable) {
                // Write key by name mode.
                output.write(2);
                String className = serializable.getClass()
                                               .getName();

                output.write(SkippedBase256.intToBuf(className.length()));
                output.write(className.getBytes(StandardCharsets.UTF_8));

                output.write(serializable.serialize());
            } else {
                throw new IllegalArgumentException("The object '" + o.getClass()
                                                                     .getName() + "' are not serializable");
            }
        }
    }

    @Override
    public Map<K, V> deserialize(BytesReader reader) {
        try {
            Map<K, V> map = ApricotCollectionFactor.hashMap();

            reader.flag();

            if (reader.read() == - 1) {
                return map;
            }

            reader.back();

            while (reader.readable(1)) {
                read(reader,
                     map
                );
            }

            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void read(BytesReader reader, Map<?, ?> map) throws Exception {
        Object key = read(reader);

        Object value = read(reader);

        map.put(EntrustEnvironment.cast(key),
                EntrustEnvironment.cast(value)
        );
    }

    private static Object read(BytesReader reader) throws Exception {
        Object reading;

        int readingMode = reader.read();
        switch (readingMode) {
            case 1 -> {
                long id = SkippedBase256.readLong(reader);
                BytesSerializer<?> serializer = KalmiaEnv.BYTES_SERIALIZE_FRAMEWORK.getSerializer(id);
                reading = serializer.deserialize(reader);
            }
            case 2 -> {
                int nameLength = SkippedBase256.readInt(reader);
                String className = new String(reader.read(nameLength),
                                              StandardCharsets.UTF_8
                );
                Constructor<?> constructor = Class.forName(className)
                                                  .getConstructor();
                if (constructor.newInstance() instanceof BytesSerializable<?> serializable) {
                    serializable.deserialize(reader);
                    reading = serializable;
                } else {
                    throw new IllegalArgumentException("The class '" + className + "' are not byte serializable");
                }
            }
            default ->
                    throw new IllegalArgumentException("Unable to deserialize object using mode '" + readingMode + "'");
        }

        return reading;
    }
}
