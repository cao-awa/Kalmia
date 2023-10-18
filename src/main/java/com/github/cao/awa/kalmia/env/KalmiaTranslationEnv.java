package com.github.cao.awa.kalmia.env;

import com.github.cao.awa.kalmia.framework.translation.event.TranslationEventFramework;
import com.github.cao.awa.kalmia.framework.translation.packet.TranslationPacketFramework;

public class KalmiaTranslationEnv {
    public static final TranslationEventFramework translationEventFramework = new TranslationEventFramework();
    public static final TranslationPacketFramework translationPacketFramework = new TranslationPacketFramework();

    public static void setupFrameworks() {
        translationEventFramework.work();
        translationPacketFramework.work();
    }
}
