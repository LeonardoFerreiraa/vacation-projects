package br.com.leonardoferreira.primavera.metadata;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
public class ComponentMetadata<T> {

    private final String name;

    private final Class<T> type;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final T instance;

}
