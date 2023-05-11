package com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust;

import com.alibaba.fastjson2.JSONArray;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingConsumer;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingFunction;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingRunnable;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.function.ExceptingSupplier;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.receptacle.Receptacle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Entrust action to a no exception environment.
 *
 * @author cao_awa
 * @author zhuaidadaya
 * @author 草二号机
 * @since 1.0.0
 */
public class EntrustEnvironment {
    @NotNull
    public static <T> T getNotNull(@Nullable T target, @NotNull T defaultValue) {
        return target == null ? defaultValue : target;
    }

    public static <T> void notNull(T target, ExceptingConsumer<T> action) {
        if (target == null) {
            return;
        }
        trys(() -> action.accept(target));
    }

    /**
     * Do action, and ignored exception.
     *
     * @param action
     *         Action
     * @author cao_awa
     * @since 1.0.0
     */
    public static void trys(ExceptingRunnable action) {
        try {
            action.apply();
        } catch (Exception ignored) {

        }
    }

    public static <T> T get(ExceptingSupplier<T> supplier, T defaultValue) {
        return trys(supplier, ex -> defaultValue);
    }

    /**
     * Do action, and handle exception.
     *
     * @param action
     *         Action
     * @author cao_awa
     * @since 1.0.0
     */
    public static void trys(ExceptingRunnable action, Consumer<Exception> whenException) {
        try {
            action.apply();
        } catch (Exception e) {
            whenException.accept(e);
        }
    }

    /**
     * Do action, and handle exception.
     *
     * @param action
     *         Action
     * @author cao_awa
     * @since 1.0.0
     */
    public static <T> T trys(ExceptingSupplier<T> action, Function<Exception, T> actionWhenException) {
        try {
            return action.get();
        } catch (Exception e) {
            return actionWhenException.apply(e);
        }
    }

    public static <T> void nulls(T target, ExceptingConsumer<T> asNull) {
        if (target == null) {
            trys(() -> asNull.accept(null));
        }
    }

    public static <T> void nulls(T target, ExceptingConsumer<T> asNull, ExceptingConsumer<T> asNotNull) {
        if (target == null) {
            trys(() -> asNull.accept(null));
        } else {
            trys(() -> asNotNull.accept(target));
        }
    }


    public static <T> T operation(T target, ExceptingConsumer<T> action) {
        trys(() -> action.accept(target));
        return target;
    }

    /**
     * Do action to compute object, and ignored exception.
     *
     * @param target
     *         Compute target
     * @param action
     *         Action
     * @author cao_awa
     * @since 1.0.0
     */
    public static <T, R> R result(T target, ExceptingFunction<T, R> action) {
        try {
            return action.apply(target);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Do action to get object, and care exception.
     * <br>
     * Get other when excepting.
     * <br>
     * Ignored exception when handling exception.
     * <br>
     *
     * @param action
     *         Action
     * @param whenException
     *         Action when exception
     * @author cao_awa
     * @since 1.0.0
     */
    public static <T> T trys(ExceptingSupplier<T> action, ExceptingSupplier<T> whenException) {
        try {
            return action.get();
        } catch (Exception e) {
            return trys(whenException);
        }
    }

    /**
     * Do action to get object, and ignored exception.
     *
     * @param action
     *         Action
     * @author cao_awa
     * @since 1.0.0
     */
    public static <T> T trys(ExceptingSupplier<T> action) {
        try {
            return action.get();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Do action, and care exception.
     * <br>
     * Ignored exception when handling exception.
     * <br>
     *
     * @param action
     *         Action
     * @param whenException
     *         Action when exception
     * @author cao_awa
     * @since 1.0.0
     */
    public static void trys(ExceptingRunnable action, ExceptingRunnable whenException) {
        try {
            action.apply();
        } catch (Exception e) {
            trys(whenException);
        }
    }

    /**
     * Entrust for function.
     *
     * @param input
     *         Source function
     * @param <I>
     *         Input type
     * @param <R>
     *         Result type
     * @return No exception environment
     *
     * @author cao_awa
     * @author zhuaidadaya
     * @since 1.0.0
     */
    public static <I, R> Function<I, R> function(ExceptingFunction<I, R> input) {
        return s -> result(
                s,
                input
        );
    }

    /**
     * Compare objects, do action if equals.
     *
     * @param source
     *         Source
     * @param target
     *         Target
     * @param action
     *         Action when equals
     * @author cao_awa
     * @since 1.0.0
     */
    public static void equals(Object source, Object target, ExceptingRunnable action) {
        if (Objects.equals(
                source,
                target
        )) {
            trys(action);
        }
    }

    /**
     * Compare objects, do action if equals.
     *
     * @param source
     *         Source
     * @param target
     *         Target
     * @param action
     *         Action when equals
     * @param orElse
     *         Action when no equals
     * @author cao_awa
     * @since 1.0.0
     */
    public static void equals(Object source, Object target, ExceptingRunnable action, ExceptingRunnable orElse) {
        if (Objects.equals(
                source,
                target
        )) {
            trys(action);
        } else {
            trys(orElse);
        }
    }

    /**
     * Compare objects, do action if equals.
     *
     * @param source
     *         Source
     * @param target1
     *         Target 1
     * @param action1
     *         Action when equals target 1
     * @param target2
     *         Target 2
     * @param action2
     *         Action when equals target 2
     * @author cao_awa
     * @since 1.0.0
     */
    public static void equals(Object source, Object target1, ExceptingRunnable action1, Object target2, ExceptingRunnable action2) {
        if (Objects.equals(
                source,
                target1
        )) {
            trys(action1);
        } else if (Objects.equals(
                source,
                target2
        )) {
            trys(action2);
        }
    }

    /**
     * Compare objects, do action if equals.
     *
     * @param source
     *         Source
     * @param target1
     *         Target 1
     * @param action1
     *         Action when equals target 1
     * @param target2
     *         Target 2
     * @param action2
     *         Action when equals target 2
     * @param orElse
     *         Action when no equals
     * @author cao_awa
     * @since 1.0.0
     */
    public static void equals(Object source, Object target1, ExceptingRunnable action1, Object target2, ExceptingRunnable action2, ExceptingRunnable orElse) {
        if (Objects.equals(
                source,
                target1
        )) {
            trys(action1);
        } else if (Objects.equals(
                source,
                target2
        )) {
            trys(action2);
        } else {
            trys(orElse);
        }
    }

    public static <T> T select(T[] array, int index) {
        return array.length > index ? array[index] : array[array.length - 1];
    }

    public static <T> T select(T[] array, Random random) {
        return array[random.nextInt(array.length)];
    }

    public static <T> T select(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    public static <T> T select(JSONArray list, Random random) {
        return cast(list.get(random.nextInt(list.size())));
    }

    public static <T> T desert(List<T> list, Random random) {
        T result = list.get(random.nextInt(list.size()));
        list.remove(result);
        return result;
    }

    /**
     * Create a receptacle, do something operation and return value of this receptacle.
     *
     * @param action
     *         Operation
     * @param <T>
     *         Type
     * @return Value of receptacle
     *
     * @author cao_awa
     * @since 1.0.0
     */
    public static <T> T receptacle(ExceptingConsumer<Receptacle<T>> action) {
        return operation(Receptacle.of(), action).get();
    }

    /**
     * Cast an object.
     *
     * @param target Cast target
     * @param <T> Cast type
     * @return Target type or null
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> T cast(@NotNull Object target) {
        return trys(() -> (T) target);
    }

    /**
     * Cast an object.
     *
     * @param target Cast target
     * @param <T>    Cast type
     * @return Target type or null
     */
    @Nullable
    public static <T> T cast(@Nullable Object target, Class<T> clazz) {
        return trys(() -> clazz.cast(target));
    }

    /**
     * Create a thread of target action.
     *
     * @param action Action
     * @return The thread of target action
     * @author 草二号机
     * @since 1.0.0
     */
    @NotNull
    public static Thread thread(Runnable action) {
        return new Thread(action);
    }

//    /**
//     * Create a virtual thread of target action.
//     *
//     * @param action Action
//     * @return The thread of target action
//     *
//     * @author 草二号机
//     * @since 1.0.0
//     */
//    @NotNull
//    public static Thread vThread(Temporary action) {
//        return Thread.ofVirtual().unstarted(action::apply);
//    }
}

