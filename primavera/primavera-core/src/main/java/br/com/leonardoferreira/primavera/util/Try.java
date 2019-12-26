package br.com.leonardoferreira.primavera.util;

import java.util.concurrent.Callable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Try {

    public static <T> T rethrowAsRuntime(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T silently(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final Throwable e) {
            return null;
        }
    }

}
