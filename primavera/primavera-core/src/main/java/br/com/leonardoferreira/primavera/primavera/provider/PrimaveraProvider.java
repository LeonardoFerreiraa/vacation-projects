package br.com.leonardoferreira.primavera.primavera.provider;

import br.com.leonardoferreira.primavera.primavera.Primavera;
import br.com.leonardoferreira.primavera.primavera.annotation.AnnotationFinder;
import br.com.leonardoferreira.primavera.primavera.decorator.ClassList;
import br.com.leonardoferreira.primavera.primavera.decorator.ComponentList;
import br.com.leonardoferreira.primavera.primavera.metadata.ComponentMetaData;
import br.com.leonardoferreira.primavera.primavera.scanner.ClasspathScanner;
import br.com.leonardoferreira.primavera.primavera.stereotype.Component;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class PrimaveraProvider implements Primavera {

    protected final ComponentList components = new ComponentList();

    protected ClassList classes = new ClassList();

    @Override
    public void scan(final Class<?> baseClass) {
        this.classes = new ClassList(ClasspathScanner.scan(baseClass));
        classes.forEach(
                c -> AnnotationFinder.isAnnotationPresent(c, Component.class),
                this::registerComponent
        );
    }

    @Override
    public <T> T registerComponent(final Class<T> clazz) {
        if (components.hasComponentForClass(clazz)) {
            return retrieveComponent(clazz);
        }

        return components.add(new ComponentMetaData<>(
                clazz,
                clazz.getSimpleName(),
                newInstance(clazz)
        ));
    }

    @Override
    public <T> T retrieveComponent(final Class<T> clazz) {
        return Optional.ofNullable(components.findByType(clazz))
                .map(ComponentMetaData::getInstance)
                .orElseThrow(() -> new NoSuchElementException("component not found"));
    }

    @Override
    public ComponentList components() {
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

    private <T> T newInstance(final Class<T> clazz) {
        try {
            final Constructor<?>[] constructors = clazz.getConstructors();
            for (final Constructor<?> constructor : constructors) {
                final T instance = resolve(constructor);
                if (instance != null) {
                    return instance;
                }
            }

            throw new RuntimeException("constructor not found");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T resolve(final Constructor<?> constructor) {
        try {
            var parameters = Arrays.stream(constructor.getParameterTypes())
                    .map(this::findClass)
                    .filter(Objects::nonNull)
                    .toArray();

            return (T) constructor.newInstance(parameters);
        } catch (Exception e) {
            return null;
        }
    }

    protected Object findClass(final Class<?> type) {
        final ComponentMetaData<?> bean = components.findByType(type);

        if (bean != null) {
            return bean.getInstance();
        }

        final Class<?> clazz = classes.findFirst(it -> it.equals(type));

        if (clazz == null) {
            return null;
        }

        return registerComponent(clazz);
    }

}
