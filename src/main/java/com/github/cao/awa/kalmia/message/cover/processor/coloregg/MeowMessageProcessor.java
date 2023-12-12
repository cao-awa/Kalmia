package com.github.cao.awa.kalmia.message.cover.processor.coloregg;

import com.github.cao.awa.kalmia.bootstrap.Kalmia;
import com.github.cao.awa.kalmia.env.KalmiaEnv;
import com.github.cao.awa.kalmia.message.cover.processor.MessageProcessor;
import com.github.cao.awa.kalmia.setting.user.UserSettings;
import com.github.cao.awa.kalmia.user.User;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MeowMessageProcessor extends MessageProcessor {
    public static final UUID ID = java.util.UUID.fromString("080c1c64-1236-4313-b053-5828e453203b");

    @Override
    public byte[] process(byte[] bytes, long sender) {
        User user = Kalmia.SERVER.userManager()
                                 .get(sender);

        assert user != null;

        String sourceText = new String(bytes,
                                       StandardCharsets.UTF_8
        );

        return (sourceText + KalmiaEnv.languageManager.translation(
                user.settings()
                    .get(UserSettings.LANGUAGE)
                    .languageKey(),
                "unsigned.coloregg.meow"
        )).getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public UUID id() {
        return ID;
    }
}
