package br.com.leonardoferreira.primavera.functional;

import java.util.Optional;
import java.util.concurrent.Callable;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Outcome<R, E> {

    private final R result;

    private final E error;

    public static <R, E> Outcome<R, E> success(final R result) {
        return new Outcome<>(result, null);
    }

    public static <R, E> Outcome<R, E> error(final E error) {
        return new Outcome<>(null, error);
    }

    public boolean hasResult() {
        return result != null;
    }

    public boolean hasError() {
        return error != null;
    }

    public static <T> Outcome<T, Throwable> of(final Callable<T> callable) {
        try {
            return Outcome.success(callable.call());
        } catch (Exception e) {
            return Outcome.error(e);
        }
    }

    public Optional<R> toOptional() {
        return Optional.ofNullable(getResult());
    }
}
