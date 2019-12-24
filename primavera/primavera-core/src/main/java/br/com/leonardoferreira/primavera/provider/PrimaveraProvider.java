package br.com.leonardoferreira.primavera.provider;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.collection.set.ClassSet;
import br.com.leonardoferreira.primavera.collection.set.ComponentSet;
import br.com.leonardoferreira.primavera.component.ComponentBuilder;
import br.com.leonardoferreira.primavera.metadata.ComponentMetaData;
import br.com.leonardoferreira.primavera.scanner.ClasspathScanner;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class PrimaveraProvider implements Primavera {

    protected final ComponentSet components = new ComponentSet();

    protected final ClassSet classes = new ClassSet();

    @Override
    public void scan(final Class<?> baseClass) {
        classes.addAll(ClasspathScanner.scan(baseClass));

        ComponentBuilder.forEach(classes, this)
                .register(this::registerComponent);
    }

    @Override
    public <T> T retrieveComponent(final Class<T> clazz) {
        return Optional.ofNullable(components.findByType(clazz))
                .map(ComponentMetaData::getInstance)
                .orElseThrow(() -> new NoSuchElementException("component not found " + clazz));
    }

    @Override
    public ComponentSet components() {
        return components;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<T> retrieveComponents(final Class<T> clazz) {
        return (Set<T>) components.stream()
                .filter(it -> clazz.isAssignableFrom(it.getType()))
                .map(ComponentMetaData::getInstance)
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    protected <T> T registerComponent(final ComponentMetaData<T> componentMetaData) {
        if (components.contains(componentMetaData)) {
            return retrieveComponent(componentMetaData.getType());
        }

        return (T) components.addAndReturn(componentMetaData).getInstance();
    }

}
