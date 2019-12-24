package br.com.leonardoferreira.primavera.component;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.metadata.ComponentMetaData;
import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.util.ConstructorUtils;
import br.com.leonardoferreira.primavera.util.ExceptionUtils;
import br.com.leonardoferreira.primavera.util.StringUtils;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComponentNode<T> {

    private final Class<T> componentClass;

    private final Callable<T> instanceCreator;

    private final List<Class<?>> dependencies;

    public static <T> ComponentNode<T> build(final Class<T> clazz, final Primavera primavera) {
        final List<Class<?>> dependencies = ConstructorUtils.findPrimaryConstructor(clazz)
                .map(Constructor::getParameterTypes)
                .stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());

        return new ComponentNode<>(
                clazz,
                () -> ConstructorUtils.primaryConstructorInstanceCreator(clazz, primavera),
                dependencies
        );
    }

    public ComponentMetaData<T> toComponentMetaData() {
        return AnnotationUtils.findAnnotation(componentClass, Component.class)
                .map(component -> new ComponentMetaData<>(
                        componentClass,
                        StringUtils.isBlank(component.name()) ? componentClass.getSimpleName() : component.name(),
                        ExceptionUtils.rethrowAsRuntime(instanceCreator)
                ))
                .orElseThrow();
    }

    public int getDependenciesSize() {
        return dependencies.size();
    }

}
