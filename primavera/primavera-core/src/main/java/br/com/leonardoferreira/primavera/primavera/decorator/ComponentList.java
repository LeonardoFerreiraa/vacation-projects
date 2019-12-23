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

    public <T> boolean hasComponent(final ComponentMetaData<T> componentMetaData) {
        return components.stream()
                .anyMatch(c -> c.equals(componentMetaData));
    }
}
