package br.com.leonardoferreira.primavera.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class PrimaveraCollection<T> implements Iterable<T> {

    protected abstract Collection<T> elements();

    public Optional<T> find(final Predicate<T> predicate) {
        for (final T it : elements()) {
            if (predicate.test(it)) {
                return Optional.of(it);
            }
        }

        return Optional.empty();
    }

    public boolean contains(final Predicate<T> predicate) {
        return find(predicate).isPresent();
    }

    @Override
    public Iterator<T> iterator() {
        return elements().iterator();
    }

    public Stream<T> stream() {
        return elements().stream();
    }

    public int size() {
        return elements().size();
    }

    public void add(final T t) {
        elements().add(t);
    }

    public void addAll(final Collection<? extends T> c) {
        elements().addAll(c);
    }

    public void addAll(final PrimaveraCollection<? extends T> c) {
        elements().addAll(c.elements());
    }

    public boolean contains(final T element) {
        return elements().contains(element);
    }
}
