package br.com.leonardoferreira.primavera.primavera.decorator;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ClassList {

    private final Set<Class<?>> classes = new HashSet<>();

    @SuppressWarnings("unchecked")
    public <T> T findFirst(final Predicate<Class<?>> predicate) {
        for (final Class<?> clazz : classes) {
            if (predicate.test(clazz)) {
                return (T) clazz;
            }
        }

        return null;
    }

    public void addAll(final Set<Class<?>> classes) {
        this.classes.addAll(classes);
    }

    public Stream<Class<?>> stream() {
        return classes.stream();
    }

}
