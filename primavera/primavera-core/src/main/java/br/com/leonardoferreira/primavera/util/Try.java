package br.com.leonardoferreira.primavera.util;

import java.util.concurrent.Callable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Try {

    @SneakyThrows
    public static <T> T uncheck(final Callable<T> callable) {
        return callable.call();
    }

    public static <T> T silently(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final Throwable e) {
            return null;
        }
    }

}
