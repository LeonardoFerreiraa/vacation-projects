package br.com.leonardoferreira.primavera.provider;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.collection.set.ClassSet;
import br.com.leonardoferreira.primavera.collection.set.ComponentSet;
import br.com.leonardoferreira.primavera.component.ComponentBuilder;
import br.com.leonardoferreira.primavera.metadata.ComponentMetadata;
import br.com.leonardoferreira.primavera.scanner.ClasspathScanner;
import br.com.leonardoferreira.primavera.scanner.PackageScan;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class PrimaveraProvider implements Primavera {

    protected final ComponentSet components = new ComponentSet();

    protected final ClassSet classes = new ClassSet();

    @Override
    public void scan(final Class<?> baseClass) {
        classes.addAll(ClasspathScanner.scan(baseClass));

        final Set<Class<?>> extraScanned = ServiceLoader.load(PackageScan.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .map(PackageScan::getPackage)
                .map(ClasspathScanner::scan)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        
        classes.addAll(extraScanned);

        ComponentBuilder.createBuilders(classes, this)
                .buildEach(this::registerComponent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T retrieveComponent(final Class<T> clazz) {
        return (T) components.find(it -> clazz.isAssignableFrom(it.getType()))
                .map(ComponentMetadata::getInstance)
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
                .map(ComponentMetadata::getInstance)
                .collect(Collectors.toSet());
    }

    protected <T> void registerComponent(final ComponentMetadata<T> componentMetaData) {
        if (!components.contains(componentMetaData)) {
            components.add(componentMetaData);
        }
    }

}
