package br.com.leonardoferreira.primavera.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    @SafeVarargs
    public static <K, V> Map<K, V> join(final Map<K, V>... maps) {
        final Map<K, V> result = new HashMap<>();

        for (final Map<K, V> map : maps) {
            result.putAll(map);
        }

        return result;
    }

}
