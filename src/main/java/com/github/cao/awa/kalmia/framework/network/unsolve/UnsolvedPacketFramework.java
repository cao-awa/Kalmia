package com.github.cao.awa.kalmia.framework.network.unsolve;

import com.github.cao.awa.apricot.anntation.Auto;
import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.kalmia.KalmiaEnv;
import com.github.cao.awa.kalmia.annotation.network.unsolve.AutoSolvedPacket;
import com.github.cao.awa.kalmia.framework.reflection.ReflectionFramework;
import com.github.cao.awa.kalmia.network.packet.ReadonlyPacket;
import com.github.cao.awa.kalmia.network.packet.UnsolvedPacket;
import com.github.cao.awa.kalmia.network.packet.factor.unsolve.UnsolvedPacketFactor;
import com.github.cao.awa.modmdo.annotation.platform.Client;
import com.github.cao.awa.modmdo.annotation.platform.Server;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

public class UnsolvedPacketFramework extends ReflectionFramework {
    private static final Logger LOGGER = LogManager.getLogger("UnsolvedPacketFramework");

    public void work() {
        // Working stream...
        reflection().getTypesAnnotatedWith(Auto.class)
                    .stream()
                    .filter(this :: match)
                    .map(this :: cast)
                    .forEach(this :: build);
    }

    public boolean match(Class<?> clazz) {
        return clazz.isAnnotationPresent(AutoSolvedPacket.class) && ReadonlyPacket.class.isAssignableFrom(clazz);
    }

    public Class<? extends ReadonlyPacket<?>> cast(Class<?> clazz) {
        return EntrustEnvironment.cast(clazz);
    }

    public void build(Class<? extends ReadonlyPacket<?>> readonly) {
//        LOGGER.info("Building function for: {}",
//                    readonly.getName()
//        );

        String mode = readonly.isAnnotationPresent(Server.class) ? "S" : readonly.isAnnotationPresent(Client.class) ? "C" : "Unknown";

        if (KalmiaEnv.isServer) {
            if (mode.equals("C")) {
                return;
            }
        } else {
            if (mode.equals("S")) {
                return;
            }
        }
        Function<byte[], UnsolvedPacket<?>> function = (data) -> new AutoUnsolved(data,
                                                                                  readonly
        );

        long id = readonly.getAnnotation(AutoSolvedPacket.class)
                          .value();

        LOGGER.info("Register unsolved {} ({} mode): {}",
                    id,
                    mode,
                    readonly.getName()
        );
        UnsolvedPacketFactor.register(id,
                                      function
        );
    }

    private static final class AutoUnsolved extends UnsolvedPacket<ReadonlyPacket<?>> {
        private final Class<? extends ReadonlyPacket<?>> clazz;

        public AutoUnsolved(byte[] data, Class<? extends ReadonlyPacket<?>> clazz) {
            super(data);
            this.clazz = clazz;
        }

        @Override
        public ReadonlyPacket<?> packet() {
            return EntrustEnvironment.trys(() -> EntrustEnvironment.cast(ensureAccessible(this.clazz.getConstructor(BytesReader.class)).newInstance(reader())),
                                           ex -> {
                                               return EntrustEnvironment.trys(() -> ensureAccessible(this.clazz.getConstructor()).newInstance(),
                                                                              e -> {
                                                                                  e.printStackTrace();
                                                                                  return null;
                                                                              }
                                               );
                                           }
            );
        }
    }
}
