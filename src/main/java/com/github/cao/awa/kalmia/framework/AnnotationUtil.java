package com.github.cao.awa.kalmia.framework;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.cao.awa.kalmia.annotation.auto.event.AutoHandler;
import com.github.cao.awa.kalmia.plugin.internal.kalmia.core.server.handler.handshake.crypto.aes.HandshakeAesCipherHandler;

import java.lang.annotation.Annotation;
import java.util.Set;

public class AnnotationUtil {
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotation) {
        if (clazz == null) {
            return null;
        }

        T anno = clazz.getAnnotation(annotation);

        if (anno == null) {
            anno = getAnnotation(
                    clazz.getSuperclass(),
                    annotation
            );

            if (anno == null) {
                for (Class<?> supers : clazz.getInterfaces()) {
                    if (anno == null) {
                        anno = getAnnotation(
                                supers,
                                annotation
                        );
                    } else {
                        break;
                    }
                }
            }
        }

        return anno;
    }

    public static <T extends Annotation> T getAnnotations(Class<?> clazz, Class<T> annotation, Set<T> result) {
        if (clazz == null) {
            return null;
        }

        T anno = clazz.getAnnotation(annotation);

        if (anno != null) {
            result.add(anno);
        }

        anno = getAnnotations(
                clazz.getSuperclass(),
                annotation,
                result
        );

        if (anno != null) {
            result.add(anno);
        }

        for (Class<?> supers : clazz.getInterfaces()) {
            if (anno == null) {
                anno = getAnnotations(
                        supers,
                        annotation,
                        result
                );
            } else {
                result.add(anno);
            }
        }

        if (anno == null) {
            return null;
        }

        result.add(anno);

        return anno;
    }

    public static <T extends Annotation> Set<T> getAnnotations(Class<?> clazz, Class<T> annotation) {
        Set<T> result = ApricotCollectionFactor.hashSet();

        getAnnotations(clazz,
                       annotation,
                       result
        );

        return result;
    }

    public static void main(String[] args) {
        System.out.println(getAnnotations(HandshakeAesCipherHandler.class,
                                          AutoHandler.class
        ));
    }
}
