package br.com.leonardoferreira.primavera.util;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.annotation.PrimaryConstructor;
import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstructorUtils {

    public static Optional<Constructor<?>> findPrimaryConstructor(final Class<?> clazz) {
        final PrimaveraList<Constructor<?>> constructors = PrimaveraList.of(clazz.getConstructors());

        if (constructors.size() == 1) {
            return Optional.of(constructors.first());
        }

        return constructors.find(constructor ->
                AnnotationUtils.isAnnotationPresent(constructor, PrimaryConstructor.class));
    }

    @SuppressWarnings("unchecked")
    public static <T> T primaryConstructorInstanceCreator(final Class<T> clazz, final Primavera primavera) {
        return (T) ConstructorUtils.findPrimaryConstructor(clazz)
                .map(constructor -> Try.silently(() -> createInstance(constructor, primavera)))
                .orElseThrow(() -> new RuntimeException("constructor not found for type " + clazz));
    }

    @SuppressWarnings("unchecked")
    private static <T> T createInstance(final Constructor<?> constructor, final Primavera primavera) throws Exception {
        var parameters = Arrays.stream(constructor.getParameterTypes())
                .map(primavera::retrieveComponent)
                .filter(Objects::nonNull)
                .toArray();

        return (T) constructor.newInstance(parameters);
    }


}
