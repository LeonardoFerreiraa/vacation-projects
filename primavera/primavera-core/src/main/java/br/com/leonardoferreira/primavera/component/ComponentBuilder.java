package br.com.leonardoferreira.primavera.component;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.collection.set.ClassSet;
import br.com.leonardoferreira.primavera.collection.set.PrimaveraSet;
import br.com.leonardoferreira.primavera.metadata.ComponentMetaData;
import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import br.com.leonardoferreira.primavera.util.ConstructorUtils;
import br.com.leonardoferreira.primavera.util.ExceptionUtils;
import br.com.leonardoferreira.primavera.util.StringUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Data
@AllArgsConstructor
public class ComponentBuilder {

    private final PrimaveraSet<ComponentNode<?>> nodes;

    public static ComponentBuilder forEach(final ClassSet classes, final Primavera primavera) {
        final Stream<ComponentNode<?>> classComponents = classes.stream()
                .filter(clazz -> AnnotationUtils.isAnnotationPresent(clazz, Component.class))
                .map(clazz -> buildComponentNodeByClass(clazz, primavera));

        final Stream<ComponentNode<?>> methodComponents = classes.methods()
                .filter(pair -> AnnotationUtils.isAnnotationPresent(pair.getValue(), Component.class))
                .map(pair -> buildComponentNodeByMethod(pair.getKey(), pair.getValue(), pair.getValue().getReturnType(), primavera));

        final PrimaveraSet<ComponentNode<?>> nodes = Stream.concat(classComponents, methodComponents)
                .sorted()
                .collect(Collectors.toCollection(PrimaveraSet::new));

        final ComponentSorter sorter = new ComponentSorter();
        for (final ComponentNode<?> node : nodes) {
            sorter.insertionSort(nodes, node);
        }

        return new ComponentBuilder(sorter.getComponents());
    }

    private static <T> ComponentNode<T> buildComponentNodeByMethod(final Class<?> clazz,
                                                                   final Method method,
                                                                   final Class<T> componentType,
                                                                   final Primavera primavera) {
        return new ComponentNode<>(
                AnnotationUtils.findAnnotation(method, Component.class).orElseThrow(),
                componentType,
                () -> methodInstanceCreator(clazz, method, primavera),
                methodDependencies(method, clazz)
        );
    }

    private static PrimaveraSet<Class<?>> methodDependencies(final Method method, final Class<?> clazz) {
        final PrimaveraSet<Class<?>> dependencies = new PrimaveraSet<>();

        dependencies.addAll(Arrays.asList(method.getParameterTypes()));
        dependencies.add(clazz);

        return dependencies;
    }

    @SuppressWarnings("unchecked")
    private static <T> T methodInstanceCreator(final Class<?> clazz,
                                               final Method method,
                                               final Primavera primavera) throws Exception {
        final Object[] args = Arrays.stream(method.getParameterTypes())
                .map(primavera::retrieveComponent)
                .toArray();

        return (T) method.invoke(primavera.retrieveComponent(clazz), args);
    }

    private static <T> ComponentNode<T> buildComponentNodeByClass(final Class<T> clazz, final Primavera primavera) {
        return new ComponentNode<>(
                AnnotationUtils.findAnnotation(clazz, Component.class).orElseThrow(),
                clazz,
                () -> ConstructorUtils.primaryConstructorInstanceCreator(clazz, primavera),
                classDependencies(clazz)
        );
    }

    private static <T> PrimaveraSet<Class<?>> classDependencies(final Class<T> clazz) {
        return ConstructorUtils.findPrimaryConstructor(clazz)
                .map(Constructor::getParameterTypes)
                .stream()
                .flatMap(Arrays::stream)
                .collect(Collectors.toCollection(PrimaveraSet::new));
    }

    public void register(final Consumer<? super ComponentMetaData<?>> action) {
        nodes.stream()
                .map(ComponentNode::toComponentMetaData)
                .forEach(action);
    }

    private static class ComponentSorter {

        @Getter
        private final PrimaveraSet<ComponentNode<?>> components = new PrimaveraSet<>();

        public void insertionSort(final PrimaveraSet<ComponentNode<?>> unsortedNodes, final ComponentNode<?> node) {
            if (alreadyInserted(node.componentClass)) {
                return;
            }

            node.dependencies.stream()
                    .filter(dependency -> !alreadyInserted(dependency))
                    .map(dependency -> unsortedNodes.findFirst(cn -> dependency.isAssignableFrom(cn.componentClass)))
                    .forEach(dependency -> insertionSort(unsortedNodes, dependency));

            components.add(node);
        }

        private boolean alreadyInserted(final Class<?> componentClass) {
            return components.contains(cn -> componentClass.isAssignableFrom(cn.componentClass));
        }

    }

    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class ComponentNode<T> implements Comparable<ComponentNode<?>> {

        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        final Component component;

        final Class<T> componentClass;

        @ToString.Exclude
        final Callable<T> instanceCreator;

        final PrimaveraSet<Class<?>> dependencies;

        public ComponentMetaData<T> toComponentMetaData() {
            return new ComponentMetaData<>(
                    componentClass,
                    StringUtils.isBlank(component.name()) ? componentClass.getSimpleName() : component.name(),
                    ExceptionUtils.rethrowAsRuntime(instanceCreator)
            );
        }

        @ToString.Include(name = "dependenciesSize", rank = 1)
        public int getDependenciesSize() {
            return dependencies.size();
        }

        @Override
        public int compareTo(final ComponentNode<?> other) {
            return Integer.compare(getDependenciesSize(), other.getDependenciesSize());
        }

    }

}
