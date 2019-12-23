package br.com.leonardoferreira.primavera.primavera.functional;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class Pair<K, V> {

    private final K key;

    private final V value;

}
