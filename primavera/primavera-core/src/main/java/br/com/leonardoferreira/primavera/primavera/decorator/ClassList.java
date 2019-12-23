package br.com.leonardoferreira.primavera.primavera.decorator;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public void forEach(final Predicate<Class<?>> filter, final Consumer<Class<?>> consumer) {
        classes.stream()
                .filter(filter)
                .forEach(consumer);
    }

    public void addAll(final Set<Class<?>> classes) {
        this.classes.addAll(classes);
    }

    public <T> Set<Class<?>> findAllImplementations(final Class<T> clazz) {
        return classes.stream()
                .filter(it -> clazz.isAssignableFrom(it))
                .filter(it -> !it.isInterface())
                .collect(Collectors.toSet());
    }

}
