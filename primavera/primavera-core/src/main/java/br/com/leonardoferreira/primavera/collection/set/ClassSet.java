package br.com.leonardoferreira.primavera.collection.set;

import br.com.leonardoferreira.primavera.functional.Pair;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

public class ClassSet extends PrimaveraSet<Class<?>> {

    public Stream<Pair<Class<?>, Method>> methods() {
        return elements.stream()
                .map(clazz -> Pair.of(clazz, clazz.getDeclaredMethods()))
                .flatMap(pair -> Arrays.stream(pair.getValue()).map(method -> Pair.of(pair.getKey(), method)));
    }

}
