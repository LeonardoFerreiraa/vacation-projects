package br.com.leonardoferreira.primavera.primavera.decorator;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Data;

@Data
public class AbstractListDecorator<T> {

    protected Set<T> elements;

    public AbstractListDecorator() {
        this.elements = new HashSet<>();
    }

    public T findFirst(final Predicate<T> predicate) {
        for (final T it : elements) {
            if (predicate.test(it)) {
                return it;
            }
        }

        return null;
    }

    public T add(final T element) {
        elements.add(element);
        return element;
    }

    public boolean has(final T element) {
        return stream().anyMatch(element::equals);
    }

    public void addAll(final Set<T> elements) {
        this.elements.addAll(elements);
    }

    public Stream<T> stream() {
        return elements.stream();
    }

}
