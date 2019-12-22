package br.com.leonardoferreira.primavera.primavera.decorator;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ClassList {

    private final Set<Class<?>> classes;

    public ClassList(final Set<Class<?>> classes) {
        this.classes = classes;
    }

    public ClassList() {
        this.classes = Collections.emptySet();
    }

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

}
