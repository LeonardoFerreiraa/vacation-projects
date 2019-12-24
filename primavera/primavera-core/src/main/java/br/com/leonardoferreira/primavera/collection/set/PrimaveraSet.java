package br.com.leonardoferreira.primavera.collection.set;

import br.com.leonardoferreira.primavera.collection.PrimaveraCollection;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class PrimaveraSet<T> extends PrimaveraCollection<T> {

    protected Set<T> elements;

    public PrimaveraSet() {
        this.elements = new LinkedHashSet<>();
    }

    @Override
    protected Collection<T> elements() {
        return elements;
    }

}
