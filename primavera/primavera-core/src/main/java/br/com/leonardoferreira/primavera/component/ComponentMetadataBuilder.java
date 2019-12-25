package br.com.leonardoferreira.primavera.component;

import br.com.leonardoferreira.primavera.collection.set.PrimaveraSet;
import br.com.leonardoferreira.primavera.metadata.ComponentMetadata;
import br.com.leonardoferreira.primavera.util.ExceptionUtils;
import java.util.concurrent.Callable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
public class ComponentMetadataBuilder<T> implements Comparable<ComponentMetadataBuilder<?>> {

    private final String name;

    private final Class<T> type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Callable<T> instanceCreator;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final PrimaveraSet<Class<?>> dependencies;

    @ToString.Include(name = "dependenciesSize")
    public int getDependenciesSize() {
        return dependencies.size();
    }

    @Override
    public int compareTo(final ComponentMetadataBuilder<?> other) {
        return Integer.compare(getDependenciesSize(), other.getDependenciesSize());
    }

    public ComponentMetadata<T> build() {
        return ExceptionUtils.rethrowAsRuntime(() ->
                new ComponentMetadata<>(
                        name,
                        type,
                        instanceCreator.call()
                ));
    }
}
