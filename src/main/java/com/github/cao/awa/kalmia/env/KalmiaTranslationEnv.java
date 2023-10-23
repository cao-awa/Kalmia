package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.framework.translation.event.TranslationEventFramework;
import com.github.cao.awa.kalmia.framework.translation.packet.TranslationPacketFramework;
import com.github.cao.awa.kalmia.network.router.kalmia.RequestRouter;
import com.github.cao.awa.kalmia.network.router.translation.TranslationRouter;

import java.util.List;
import java.util.Map;

public class KalmiaTranslationEnv {
    public static final TranslationEventFramework translationEventFramework = new TranslationEventFramework();
    public static final TranslationPacketFramework translationPacketFramework = new TranslationPacketFramework();
    public static final Map<String, String> identityMap = ApricotCollectionFactor.hashMap();
    public static final Map<String, List<String>> identityReverseMap = ApricotCollectionFactor.hashMap();
    public static final Map<String, TranslationRouter> routers = ApricotCollectionFactor.hashMap();
    public static final Map<TranslationRouter, RequestRouter> mainRouter = ApricotCollectionFactor.hashMap();

    public static void identity(RequestRouter requestRouter, TranslationRouter translationRouter) {
        identityMap.put(requestRouter.metadata()
                                     .formatConnectionId(),
                        translationRouter.clientIdentity()
        );

        identityReverseMap.compute(translationRouter.clientIdentity(),
                                   (key, value) -> {
                                       if (value == null) {
                                           value = ApricotCollectionFactor.arrayList();
                                       }
                                       value.add(requestRouter.metadata()
                                                              .formatConnectionId());
                                       return value;
                                   }
        );

        routers.put(translationRouter.clientIdentity(),
                    translationRouter
        );
    }

    public static void clearIdentity(RequestRouter requestRouter) {
        String connectionId = requestRouter.metadata()
                                           .formatConnectionId();
        String translationIdentity = identityMap.get(connectionId);
        identityMap.remove(connectionId);
        List<String> kalmiaConnections = identityReverseMap.get(translationIdentity);
        if (kalmiaConnections != null) {
            kalmiaConnections.remove(connectionId);
        }
    }

    public static void clearIdentity(TranslationRouter translationRouter) {
        List<String> kalmiaConnections = identityReverseMap.get(translationRouter.clientIdentity());
        if (kalmiaConnections != null) {
            for (String connectionId : kalmiaConnections) {
                identityMap.remove(connectionId);
            }
        }
        identityReverseMap.remove(translationRouter.clientIdentity());

        routers.remove(translationRouter.clientIdentity());
    }

    public static TranslationRouter translationRouter(RequestRouter requestRouter) {
        return routers.get(identityMap.get(requestRouter.metadata()
                                                        .formatConnectionId()));
    }

    public static void router(TranslationRouter translationRouter, RequestRouter requestRouter) {
        mainRouter.put(translationRouter,
                       requestRouter
        );
    }

    public static RequestRouter router(TranslationRouter translationRouter) {
        return mainRouter.get(translationRouter);
    }

    public static void clearRouter(TranslationRouter translationRouter) {
        mainRouter.remove(translationRouter);
    }

    public static void setupFrameworks() {
        translationEventFramework.work();
        translationPacketFramework.work();
    }
}
