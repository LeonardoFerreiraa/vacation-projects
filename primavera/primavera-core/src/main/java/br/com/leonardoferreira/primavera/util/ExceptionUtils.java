package br.com.leonardoferreira.primavera.util;

import java.util.concurrent.Callable;

public class ExceptionUtils {

    public static <T> T rethrowAsRuntime(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
