package br.com.leonardoferreira.primavera.primavera.metadata;

public class ComponentMetaData<T> {

    private final Class<T> type;

    private final String name;

    private final T instance;

    public ComponentMetaData(final Class<T> type, final String name, final T instance) {
        this.type = type;
        this.name = name;
        this.instance = instance;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public T getInstance() {
        return instance;
    }
}
