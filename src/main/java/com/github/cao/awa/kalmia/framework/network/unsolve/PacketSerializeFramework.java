package com.github.cao.awa.kalmia.framework.network.unsolve;

import com.github.cao.awa.apricot.annotation.auto.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.auto.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.trtr.util.string.StringConcat;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class PacketSerializeFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("UnsolvedPacketFramework");
    private final Map<Class<? extends Packet<?>>, Constructor<? extends Packet<?>>> constructors = ApricotCollectionFactor.hashMap();
    private final Map<Class<? extends Packet<?>>, byte[]> ids = ApricotCollectionFactor.hashMap();

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(AutoSolvedPacket.class) && Packet.class.isAssignableFrom(clazz);
    }

    public Class<? extends Packet<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends Packet<?>> packet) {
        long id = packet.getAnnotation(AutoSolvedPacket.class)
                        .value();

        Constructor<? extends Packet<?>> constructor = EntrustEnvironment.trys(() -> EntrustEnvironment.cast(ensureAccessible(packet.getConstructor(BytesReader.class))),
                                                                               ex -> {
                                                                                   return EntrustEnvironment.trys(() -> ensureAccessible(packet.getConstructor()),
                                                                                                                  ex0 -> {
                                                                                                                      BugTrace.trace(ex0,
                                                                                                                                     StringConcat.concat(
                                                                                                                                             "The packet '",
                                                                                                                                             packet.getName(),
                                                                                                                                             "' are missing the standard constructor, but it using @AutoSolvedPacket annotation to invert control by id '",
                                                                                                                                             id,
                                                                                                                                             "'"
                                                                                                                                     ),
                                                                                                                                     true
                                                                                                                      );
                                                                                                                      return null;
                                                                                                                  }
                                                                                   );
                                                                               }
        );

        if (constructor == null) {
            BugTrace.trace(StringConcat.concat("Auto solved packet '",
                                               packet.getName(),
                                               "' missing constructor"
                           ),
                           true
            );
        } else {
            this.constructors.put(packet,
                                  constructor
            );
            this.ids.put(packet,
                         SkippedBase256.longToBuf(id)
            );
        }

        Function<byte[], UnsolvedPacket<?>> function = (data) -> new AutoUnsolved(data,
                                                                                  packet,
                                                                                  this
        );

        LOGGER.info("Register unsolved {}: {}",
                    id,
                    packet.getName()
        );
        UnsolvedPacketFactor.register(id,
                                      function
        );
    }

    public Packet<?> solve(Class<? extends Packet<?>> clazz, BytesReader reader) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        try {
            return this.constructors.get(clazz)
                                    .newInstance(reader);
        } catch (Exception e) {
            BugTrace.trace(e,
                           "Failed solve packet"
            );
            return solve(clazz);
        }
    }

    public Packet<?> solve(Class<? extends Packet<?>> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        return this.constructors.get(clazz)
                                .newInstance();
    }

    public byte[] id(Class<? extends Packet<?>> type) {
        return this.ids.get(type);
    }

    public byte[] id(Packet<?> type) {
        return this.ids.get(type.getClass());
    }

    public byte[] payload(Packet<?> packet) throws Exception {
        LinkedList<Field> fields = autoFields(packet);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (Field field : fields) {
            output.write(
                    KalmiaEnv.serializeFramework.serialize(field.get(packet))
            );
        }

        return output.toByteArray();
    }

    public void create(Packet<?> packet, BytesReader reader) throws Exception {
        LinkedList<Field> fields = autoFields(packet);

        for (Field field : fields) {
            Object deserialize = KalmiaEnv.serializeFramework.deserialize(field.getType(),
                                                                          reader
            );
            field.set(packet,
                      deserialize
            );
        }
    }

    private LinkedList<Field> autoFields(Packet<?> packet) throws NoSuchFieldException {
        Class<Packet<?>> clazz = EntrustEnvironment.cast(packet.getClass());
        assert clazz != null;
        LinkedList<Field> fields = ApricotCollectionFactor.linkedList();
        for (Field e : clazz.getDeclaredFields()) {
            if (e.isAnnotationPresent(AutoData.class)) {
                fields.add(ensureAccessible(clazz.getDeclaredField(e.getName()),
                                            packet
                ));
            }
        }
        return fields;
    }

    private static final class AutoUnsolved extends UnsolvedPacket<Packet<?>> {
        private final Class<? extends Packet<?>> clazz;
        private final PacketSerializeFramework framework;

        public AutoUnsolved(byte[] data, Class<? extends Packet<?>> clazz, PacketSerializeFramework framework) {
            super(data);
            this.clazz = clazz;
            this.framework = framework;
        }

        @Override
        public Packet<?> packet() {
            return EntrustEnvironment.trys(this :: create,
                                           ex -> {
                                               BugTrace.trace(ex,
                                                              StringConcat.concat(
                                                                      "Readonly packet '",
                                                                      this.clazz.getName(),
                                                                      "' are missing the standard constructor, but it using @AutoSolvedPacket annotation to invert control by id '",
                                                                      this.clazz.getAnnotation(AutoSolvedPacket.class)
                                                                                .value(),
                                                                      "'"
                                                              ),
                                                              true
                                               );
                                               return null;
                                           }
            );
        }

        private Packet<?> create() throws InvocationTargetException, InstantiationException, IllegalAccessException {
            return this.framework.solve(this.clazz,
                                        reader()
                       )
                                 .receipt(receipt());
        }
    }
}
