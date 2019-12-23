package br.com.leonardoferreira.primavera.primavera.decorator;

import br.com.leonardoferreira.primavera.primavera.functional.Pair;
import br.com.leonardoferreira.primavera.primavera.metadata.ComponentMetaData;
import br.com.leonardoferreira.primavera.primavera.metadata.ComponentMethodMetaData;
import java.util.Arrays;
import java.util.stream.Stream;

public class ComponentList extends AbstractListDecorator<ComponentMetaData<?>> {

    @SuppressWarnings("unchecked")
    public <T> ComponentMetaData<T> findByType(final Class<T> clazz) {
        return (ComponentMetaData<T>) findFirst(it -> clazz.isAssignableFrom(it.getType()));
    }

    public Stream<ComponentMethodMetaData> methods() {
        return elements.stream()
                .map(component -> Pair.of(component, component.getType().getDeclaredMethods()))
                .flatMap(pair -> Arrays.stream(pair.getValue()).map(method -> Pair.of(pair.getKey(), method)))
                .map(pair -> new ComponentMethodMetaData(pair.getKey().getInstance(), pair.getValue()));
    }

}
