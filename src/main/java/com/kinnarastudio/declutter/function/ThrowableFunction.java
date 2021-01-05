package com.kinnarastudio.declutter.function;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Throwable version of {@link Function}.
 * Returns null then exception is raised
 *
 * @param <T>
 * @param <R>
 * @param <E>
 */
@FunctionalInterface
public interface ThrowableFunction<T, R, E extends Exception> extends Function<T, R> {

    @Override
    default R apply(T t) {
        try {
            return applyThrowable(t);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
            return null;
        }
    }

    R applyThrowable(T t) throws E;

    /**
     * @param f
     * @return
     */
    default Function<T, R> onException(Function<? super E, ? extends R> f) {
        return (T a) -> {
            try {
                return (R) applyThrowable(a);
            } catch (Exception e) {
                return f.apply((E) e);
            }
        };
    }

    /**
     * @param f
     * @return
     */
    default Function<T, R> onException(BiFunction<? super T, ? super E, ? extends R> f) {
        return (T a) -> {
            try {
                return (R) applyThrowable(a);
            } catch (Exception e) {
                return f.apply(a, (E) e);
            }
        };
    }
}