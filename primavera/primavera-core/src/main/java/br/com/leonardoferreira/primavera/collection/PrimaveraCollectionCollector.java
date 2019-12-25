package br.com.leonardoferreira.primavera.collection;

import br.com.leonardoferreira.primavera.collection.list.PrimaveraList;
import br.com.leonardoferreira.primavera.collection.set.PrimaveraSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PrimaveraCollectionCollector<T, A, R> implements Collector<T, A, R> {

    private final Supplier<A> supplier;
    private final BiConsumer<A, T> accumulator;
    private final BinaryOperator<A> combiner;
    private final Function<A, R> finisher;

    private PrimaveraCollectionCollector(final Supplier<A> supplier,
                                         final BiConsumer<A, T> accumulator,
                                         final BinaryOperator<A> combiner) {
        this(supplier, accumulator, combiner, castingIdentity());
    }

    public static <T, C extends PrimaveraList<T>> Collector<T, ?, C> toList() {
        return new PrimaveraCollectionCollector<>(
                PrimaveraList::new,
                PrimaveraCollection<T>::add,
                (r1, r2) -> {
                    r1.addAll(r2);
                    return r1;
                }
        );
    }

    public static <T, C extends PrimaveraSet<T>> Collector<T, ?, C> toSet() {
        return new PrimaveraCollectionCollector<>(
                PrimaveraSet::new,
                PrimaveraCollection<T>::add,
                (r1, r2) -> {
                    r1.addAll(r2);
                    return r1;
                }
        );
    }

    @SuppressWarnings("unchecked")
    private static <I, R> Function<I, R> castingIdentity() {
        return i -> (R) i;
    }

    @Override
    public Supplier<A> supplier() {
        return supplier;
    }

    @Override
    public BiConsumer<A, T> accumulator() {
        return accumulator;
    }

    @Override
    public BinaryOperator<A> combiner() {
        return combiner;
    }

    @Override
    public Function<A, R> finisher() {
        return finisher;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Collector.Characteristics.IDENTITY_FINISH);
    }
}
