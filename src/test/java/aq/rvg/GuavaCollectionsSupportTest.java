package aq.rvg;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.Predicate;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThat;

final class GuavaCollectionsSupportTest {
    @Test
    void list() {
        val value = random(new TypeToken<ImmutableList<Integer>>() { });
        assertThat(value)
                .isNotNull()
                .isNotEmpty()
                .allMatch(i -> i != 0);
    }

    @Test
    void map() {
        val keysAreNotZero = (Predicate<Map<Integer, String>>) m -> m.keySet().stream()
                .allMatch(i -> i != 0);
        val valuesAreNotEmpty = (Predicate<Map<Integer, String>>) m -> m.values().stream()
                .noneMatch(String::isEmpty);

        val value = random(new TypeToken<ImmutableMap<Integer, String>>() { });

        assertThat(value)
                .isNotNull()
                .isNotEmpty()
                .matches(keysAreNotZero)
                .matches(valuesAreNotEmpty);
    }

    @Test
    void set() {
        val value = random(new TypeToken<ImmutableSet<Integer>>() { });
        assertThat(value)
                .isNotNull()
                .isNotEmpty()
                .allMatch(i -> i != 0);
    }
}
