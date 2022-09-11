package helpers.java;

import com.google.common.collect.ImmutableMap;
import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Optional;

@UtilityClass
public final class MapHelpers {
    public static <K, V> ImmutableMap<K, V> map(K key, V value) {
        return ImmutableMap.of(key, value);
    }

    public static <K, V> ImmutableMap<K, V> map(
            K key1, V value1,
            K key2, V value2) {
        return ImmutableMap.of(key1, value1, key2, value2);
    }

    public static <K, V> ImmutableMap<K, V> map(
            K key1, V value1,
            K key2, V value2,
            K key3, V value3) {
        return ImmutableMap.of(key1, value1, key2, value2, key3, value3);
    }

    public static <K, V> Optional<V> tryToGet(Map<K, V> map, K key) {
        return Optional.ofNullable(map.get(key));
    }
}
