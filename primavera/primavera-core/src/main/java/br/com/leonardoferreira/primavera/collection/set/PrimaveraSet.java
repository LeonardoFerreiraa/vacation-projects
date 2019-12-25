package br.com.leonardoferreira.primavera.collection.set;

import br.com.leonardoferreira.primavera.collection.PrimaveraCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class PrimaveraSet<T> extends PrimaveraCollection<T> {

    protected Set<T> elements;

    public PrimaveraSet() {
        this.elements = new LinkedHashSet<>();
    }

    @SafeVarargs
    public static <T> PrimaveraSet<T> of(final T... elements) {
        final PrimaveraSet<T> set = new PrimaveraSet<>();
        set.addAll(Arrays.asList(elements));
        return set;
    }

    public static <T> PrimaveraSet<T> of(final Set<T> other) {
        final PrimaveraSet<T> set = new PrimaveraSet<>();
        set.addAll(other);
        return set;
    }

    @Override
    protected Collection<T> elements() {
        return elements;
    }

    @SafeVarargs
    public final PrimaveraSet<T> attach(final T... elements) {
        final PrimaveraSet<T> result = new PrimaveraSet<>();

        result.addAll(this.elements);
        result.addAll(Arrays.asList(elements));

        return result;
    }
}
