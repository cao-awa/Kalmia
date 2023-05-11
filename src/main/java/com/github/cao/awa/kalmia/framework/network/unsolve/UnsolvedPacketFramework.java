package com.github.cao.awa.kalmia.framework.network.unsolve;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoData;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.bug.BugTrace;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.mathematic.base.SkippedBase256;
import com.github.cao.awa.kalmia.network.packet.Packet;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Generic;
import com.github.cao.awa.modmdo.annotation.platform.Server;
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

public class UnsolvedPacketFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("UnsolvedPacketFramework");
    private final Map<Class<? extends Packet<?>>, Constructor<? extends Packet<?>>> constructors = ApricotCollectionFactor.newHashMap();
    private final Map<Class<? extends Packet<?>>, byte[]> ids = ApricotCollectionFactor.newHashMap();

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

    public void build(Class<? extends Packet<?>> readonly) {
//        LOGGER.info("Building function for: {}",
//                    readonly.getName()
//        );

        String mode = readonly.isAnnotationPresent(Generic.class) ? "C/S" : readonly.isAnnotationPresent(Server.class) ? "S" : readonly.isAnnotationPresent(Client.class) ? "C" : "Unknown";

        // Check the packet mode and cancel register.
        if (KalmiaEnv.isServer) {
            if (mode.equals("C")) {
                return;
            }
        } else {
            if (mode.equals("S")) {
                return;
            }
        }

        long id = readonly.getAnnotation(AutoSolvedPacket.class)
                          .value();

        Constructor<? extends Packet<?>> constructor = EntrustEnvironment.trys(() -> EntrustEnvironment.cast(ensureAccessible(readonly.getConstructor(BytesReader.class))),
                                                                               ex -> {
                                                                                   return EntrustEnvironment.trys(() -> ensureAccessible(readonly.getConstructor()),
                                                                                                                  ex0 -> {
                                                                                                                      BugTrace.trace(ex0,
                                                                                                                                     StringConcat.concat(
                                                                                                                                             "Readonly packet '",
                                                                                                                                             readonly.getName(),
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
                                               readonly.getName(),
                                               "' missing constructor"
                           ),
                           true
            );
        } else {
            this.constructors.put(readonly,
                                  constructor
            );
            this.ids.put(readonly,
                         SkippedBase256.longToBuf(id)
            );
        }

        Function<byte[], UnsolvedPacket<?>> function = (data) -> new AutoUnsolved(data,
                                                                                  readonly,
                                                                                  this
        );

        LOGGER.info("Register unsolved {} ({} mode): {}",
                    id,
                    mode,
                    readonly.getName()
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
            e.printStackTrace();
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

    public byte[] data(Packet<?> packet) throws Exception {
        Class<Packet<?>> clazz = EntrustEnvironment.cast(packet.getClass());
        LinkedList<Field> fields = ApricotCollectionFactor.newLinkedList();
        for (Field e : clazz.getDeclaredFields()) {
            if (e.isAnnotationPresent(AutoData.class)) {
                fields.add(ensureAccessible(clazz.getDeclaredField(e.getName()),
                                            packet
                ));
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for (Field field : fields) {
            output.write(KalmiaEnv.serializeFramework.getSerializer(field.getType())
                                                     .serialize(EntrustEnvironment.cast(field.get(packet))));
        }

        return output.toByteArray();
    }

    public void create(Packet<?> packet, BytesReader reader) throws Exception {
        Class<Packet<?>> clazz = EntrustEnvironment.cast(packet.getClass());
        LinkedList<Field> fields = ApricotCollectionFactor.newLinkedList();
        for (Field e : clazz.getDeclaredFields()) {
            if (e.isAnnotationPresent(AutoData.class)) {
                fields.add(ensureAccessible(clazz.getDeclaredField(e.getName()),
                                            packet
                ));
            }
        }

        for (Field field : fields) {
            Object deserialize = KalmiaEnv.serializeFramework.getSerializer(field.getType())
                                                             .deserialize(reader);
            field.set(packet,
                      deserialize
            );
        }
    }

    private static final class AutoUnsolved extends UnsolvedPacket<Packet<?>> {
        private final Class<? extends Packet<?>> clazz;
        private final UnsolvedPacketFramework framework;

        public AutoUnsolved(byte[] data, Class<? extends Packet<?>> clazz, UnsolvedPacketFramework framework) {
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
