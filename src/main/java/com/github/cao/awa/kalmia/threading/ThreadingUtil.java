package com.github.cao.awa.kalmia.threading;

import com.github.cao.awa.kalmia.annotations.threading.ForceMainThread;

public class ThreadingUtil {
    public static boolean forceMainThread(Class<?> clazz) {
        return clazz.isAnnotationPresent(ForceMainThread.class);
    }
}
