package br.com.leonardoferreira.primavera.collection.list;

import br.com.leonardoferreira.primavera.collection.PrimaveraCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import lombok.ToString;

@ToString
public class PrimaveraList<T> extends PrimaveraCollection<T> {

    private List<T> elements;

    public PrimaveraList() {
        elements = new ArrayList<>();
    }

    public PrimaveraList(final List<T> elements) {
        this.elements = elements;
    }

    @SafeVarargs
    public static <T> PrimaveraList<T> of(final T... elements) {
        return new PrimaveraList<>(Arrays.asList(elements));
    }

    @Override
    protected Collection<T> elements() {
        return elements;
    }

    public T first() {
        return elements.isEmpty() ? null : elements.get(0);
    }

    public T last() {
        return elements.isEmpty() ? null : elements.get(elements.size() - 1);
    }

    public T get(final int index) {
        return elements.get(index);
    }

}
