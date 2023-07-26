package com.github.cao.awa.kalmia.framework.loader;

import bot.inker.acj.JvmHacker;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarFile;

public class JarSearchLoader {
    public static List<URL> load(@NotNull File file) {
        final List<URL> urls = ApricotCollectionFactor.arrayList();
        List<File> jars = ApricotCollectionFactor.arrayList();
        if (file.exists()) {
            if (file.isFile()) {
                jars.add(file);
            } else {
                jars.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
            }

            try {
                for (File jarFile : jars) {
                    JarFile jar = new JarFile(jarFile);
                    JvmHacker.instrumentation()
                             .appendToSystemClassLoaderSearch(jar);
                    urls.add(jarFile.toURI()
                                    .toURL());
                }
            } catch (Exception ignored) {

            }
        }
        return urls;
    }
}
