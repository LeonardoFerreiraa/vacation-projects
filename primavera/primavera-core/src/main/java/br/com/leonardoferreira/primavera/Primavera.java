package br.com.leonardoferreira.primavera;

import br.com.leonardoferreira.primavera.decorator.ComponentList;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;

public interface Primavera {

    <T> T registerComponent(final Class<T> clazz);

    <T> T retrieveComponent(final Class<T> clazz);

    void run();

    void scan(final Class<?> baseClass);

    PrimaveraType type();

    ComponentList components();

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
