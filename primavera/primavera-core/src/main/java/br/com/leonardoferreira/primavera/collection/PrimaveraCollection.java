package br.com.leonardoferreira.primavera.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class PrimaveraCollection<T> implements Iterable<T>, Collection<T> {

    protected abstract Collection<T> elements();

    public T addAndReturn(final T element) {
        elements().add(element);
        return element;
    }

    public Optional<T> find(final Predicate<T> predicate) {
        return Optional.ofNullable(findFirst(predicate));
    }

    public T findFirst(final Predicate<T> predicate) {
        for (final T it : elements()) {
            if (predicate.test(it)) {
                return it;
            }
        }

        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return elements().iterator();
    }

    @Override
    public boolean contains(final Object o) {
        return elements().contains(o);
    }

    @Override
    public Stream<T> stream() {
        return elements().stream();
    }

    @Override
    public int size() {
        return elements().size();
    }

    @Override
    public boolean isEmpty() {
        return elements().isEmpty();
    }

    @Override
    public Object[] toArray() {
        return elements().toArray();
    }

    @Override
    public <T1> T1[] toArray(final T1[] a) {
        return elements().toArray(a);
    }

    @Override
    public boolean add(final T t) {
        return elements().add(t);
    }

    @Override
    public boolean remove(final Object o) {
        return elements().remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return elements().containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return elements().addAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return elements().removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return elements().retainAll(c);
    }

    @Override
    public void clear() {
        elements().clear();
    }

}
