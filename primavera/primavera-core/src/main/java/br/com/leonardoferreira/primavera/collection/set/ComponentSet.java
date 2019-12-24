package br.com.leonardoferreira.primavera.collection.set;

import br.com.leonardoferreira.primavera.functional.Pair;
import br.com.leonardoferreira.primavera.metadata.ComponentMetaData;
import br.com.leonardoferreira.primavera.metadata.ComponentMethodMetaData;
import java.util.Arrays;
import java.util.stream.Stream;

public class ComponentSet extends PrimaveraSet<ComponentMetaData<?>> {

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
