package com.github.cao.awa.kalmia.message.cover.processor.time;

import com.github.cao.awa.kalmia.message.cover.processor.MessageProcessor;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class TimeMessageProcessor extends MessageProcessor {
    public static final UUID ID = java.util.UUID.fromString("080c1c64-1236-4313-b053-5828e453203c");

    @Override
    public byte[] process(byte[] bytes, long sender) {
        String str = new String(bytes,
                                StandardCharsets.UTF_8
        );

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        str = str.replace("#{TIME}",
                          format.format(new Date())
        );

        return str.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public UUID id() {
        return ID;
    }
}
