package br.com.leonardoferreira.primavera.collection.set;

import br.com.leonardoferreira.primavera.functional.Pair;
import br.com.leonardoferreira.primavera.metadata.ComponentMetadata;
import br.com.leonardoferreira.primavera.metadata.ComponentMethodMetadata;
import java.util.Arrays;
import java.util.stream.Stream;

public class ComponentSet extends PrimaveraSet<ComponentMetadata<?>> {

    @SuppressWarnings("unchecked")
    public <T> ComponentMetadata<T> findByType(final Class<T> clazz) {
        return (ComponentMetadata<T>) findFirst(it -> clazz.isAssignableFrom(it.getType()));
    }

    public Stream<ComponentMethodMetadata> methods() {
        return elements.stream()
                .map(component -> Pair.of(component, component.getType().getDeclaredMethods()))
                .flatMap(pair -> Arrays.stream(pair.getValue()).map(method -> Pair.of(pair.getKey(), method)))
                .map(pair -> new ComponentMethodMetadata(pair.getKey().getInstance(), pair.getValue()));
    }

}
