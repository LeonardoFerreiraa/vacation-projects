package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.Primavera;
import java.lang.reflect.Method;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodUtils {

    @SuppressWarnings("unchecked")
    public static <T> T invokeWithComponents(final Class<?> clazz, final Method method, final Primavera primavera) {
        return ExceptionUtils.rethrowAsRuntime(() -> {
            final Object[] args = Arrays.stream(method.getParameterTypes())
                    .map(primavera::retrieveComponent)
                    .toArray();

            return (T) method.invoke(primavera.retrieveComponent(clazz), args);
        });
    }

}
