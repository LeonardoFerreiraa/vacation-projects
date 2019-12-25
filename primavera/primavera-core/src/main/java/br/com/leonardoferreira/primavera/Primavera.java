package br.com.leonardoferreira.primavera;

import br.com.leonardoferreira.primavera.collection.set.ComponentSet;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;

public interface Primavera {

    void run();

    void scan(final Class<?> baseClass);

    PrimaveraType type();

    ComponentSet components();

    <T> T retrieveComponent(final Class<T> clazz);

    <T> Set<T> retrieveComponents(Class<T> clazz);

    static Primavera of(final PrimaveraType type) {
        return ServiceLoader.load(Primavera.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(primavera -> type.equals(primavera.type()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("provider not found"));
    }
}
