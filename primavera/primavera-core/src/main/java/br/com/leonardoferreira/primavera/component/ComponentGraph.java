package br.com.leonardoferreira.primavera.component;

import br.com.leonardoferreira.primavera.Primavera;
import br.com.leonardoferreira.primavera.collection.set.ClassSet;
import br.com.leonardoferreira.primavera.collection.set.PrimaveraSet;
import br.com.leonardoferreira.primavera.stereotype.Component;
import br.com.leonardoferreira.primavera.util.AnnotationUtils;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComponentGraph {

    private final PrimaveraSet<ComponentNode<?>> nodes;

    public static ComponentGraph build(final ClassSet classes, final Primavera primavera) {
        final PrimaveraSet<ComponentNode<?>> nodes = classes.stream()
                .filter(clazz -> AnnotationUtils.isAnnotationPresent(clazz, Component.class))
                .map(clazz -> ComponentNode.build(clazz, primavera))
                .sorted(Comparator.comparingInt(ComponentNode::getDependenciesSize))
                .collect(Collectors.toCollection(PrimaveraSet::new));

        return new ComponentGraph(nodes);
    }

    public Stream<ComponentNode<?>> components() {
        return nodes.stream();
    }

}
