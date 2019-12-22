package br.com.leonardoferreira.primavera.primavera.decorator;

import br.com.leonardoferreira.primavera.primavera.metadata.ComponentMetaData;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class ComponentList {

    private final Set<ComponentMetaData<?>> components = new HashSet<>();

    public <T> T add(final ComponentMetaData<T> bean) {
        components.add(bean);
        return bean.getInstance();
    }

    public <T> boolean hasComponentForClass(final Class<T> clazz) {
        return components.stream()
                .anyMatch(it -> it.getType().equals(clazz));
    }

    @SuppressWarnings("unchecked")
    public <T> ComponentMetaData<T> findByType(final Class<T> clazz) {
        return (ComponentMetaData<T>) components.stream()
                .filter(it -> clazz.isAssignableFrom(it.getType()))
                .findFirst()
                .orElse(null);
    }

    public Stream<ComponentMetaData<?>> stream() {
        return components.stream();
    }
}
