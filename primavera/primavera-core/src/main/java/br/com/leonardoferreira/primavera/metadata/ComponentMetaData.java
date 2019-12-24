package br.com.leonardoferreira.primavera.metadata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"name"})
public class ComponentMetaData<T> {

    private final Class<T> type;

    private final String name;

    private final T instance;

}
