package br.com.leonardoferreira.primavera.component;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.collection.set.ClassSet;
import br.com.leonardoferreira.primavera.collection.set.PrimaveraSet;
import br.com.leonardoferreira.primavera.metadata.ComponentMetadata;
import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.util.BeanUtils;
import br.com.leonardoferreira.primavera.util.ConstructorUtils;
import br.com.leonardoferreira.primavera.util.MapUtils;
import br.com.leonardoferreira.primavera.util.MethodUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComponentBuilder {

    private final Map<Class<?>, ComponentMetadataBuilder<?>> builders;

    private final Primavera primavera;

    public static ComponentBuilder createBuilders(final ClassSet classes, final Primavera primavera) {
        final Map<Class<?>, ComponentMetadataBuilder<?>> classComponentBuilders = classes.stream()
                .filter(clazz -> AnnotationUtils.isAnnotationPresent(clazz, Component.class))
                .map(clazz -> buildBuildersByClass(clazz, primavera, classes))
                .collect(Collectors.toMap(ComponentMetadataBuilder::getType, builder -> builder));

        final Map<Class<?>, ComponentMetadataBuilder<?>> methodComponentBuilders = classes.methods()
                .filter(pair -> AnnotationUtils.isAnnotationPresent(pair.getValue(), Component.class))
                .map(pair -> buildComponentNodeByMethod(pair.getKey(), pair.getValue(), pair.getValue().getReturnType(), primavera, classes))
                .collect(Collectors.toMap(ComponentMetadataBuilder::getType, builder -> builder));

        return new ComponentBuilder(
                MapUtils.join(classComponentBuilders, methodComponentBuilders),
                primavera
        );
    }

    public void buildEach(final Consumer<ComponentMetadata<?>> register) {
        builders.forEach((key, value) -> buildComponent(key, register));
    }

    public void buildComponent(final Class<?> clazz, final Consumer<ComponentMetadata<?>> register) {
        if (alreadyInserted(clazz)) {
            return;
        }

        builders.get(clazz)
                .getDependencies()
                .stream()
                .filter(dependency -> !alreadyInserted(dependency))
                .forEach(dependency -> buildComponent(dependency, register));

        final ComponentMetadata<?> build = builders.get(clazz).build();
        register.accept(build);
    }

    private boolean alreadyInserted(final Class<?> dependency) {
        return primavera.components()
                .contains(it -> dependency.isAssignableFrom(it.getType()));
    }

    private static <T> ComponentMetadataBuilder<T> buildComponentNodeByMethod(final Class<?> clazz,
                                                                              final Method method,
                                                                              final Class<T> componentType,
                                                                              final Primavera primavera,
                                                                              final ClassSet classes) {
        PrimaveraSet<Class<?>> dependencies = Stream.concat(Arrays.stream(method.getParameterTypes()), Stream.of(clazz))
                .map(classes::findImplementation)
                .collect(Collectors.toCollection(PrimaveraSet::new));

        final Component component = AnnotationUtils.findAnnotation(method, Component.class).orElseThrow();
        return new ComponentMetadataBuilder<>(
                BeanUtils.beanName(componentType, component),
                componentType,
                () -> MethodUtils.invokeWithComponents(clazz, method, primavera),
                dependencies
        );
    }

    private static <T> ComponentMetadataBuilder<T> buildBuildersByClass(final Class<T> clazz, final Primavera primavera, final ClassSet classes) {
        final PrimaveraSet<Class<?>> dependencies = ConstructorUtils.findPrimaryConstructor(clazz)
                .map(Constructor::getParameterTypes)
                .stream()
                .flatMap(Arrays::stream)
                .map(classes::findImplementation)
                .collect(Collectors.toCollection(PrimaveraSet::new));

        final Component component = AnnotationUtils.findAnnotation(clazz, Component.class).orElseThrow();
        return new ComponentMetadataBuilder<>(
                BeanUtils.beanName(clazz, component),
                clazz,
                () -> ConstructorUtils.primaryConstructorInstanceCreator(clazz, primavera),
                dependencies
        );
    }

}
