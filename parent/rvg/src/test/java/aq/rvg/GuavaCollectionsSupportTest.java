package aq.rvg;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThat;

final class GuavaCollectionsSupportTest {
    @Test
    void list() {
        val value = random(new TypeToken<ImmutableList<Integer>>() { },
                           Config.builder()
                                   .collNeverEmpty()
                                   .build());
        assertThat(value)
                .isNotNull()
                .isNotEmpty();
    }

    @Test
    void map() {
        val valuesAreNotEmpty = (Predicate<Map<Integer, String>>) m -> m.values().stream()
                .noneMatch(String::isEmpty);

        val value = random(new TypeToken<ImmutableMap<Integer, String>>() { },
                           Config.builder().collNeverEmpty().build());

        assertThat(value)
                .isNotNull()
                .isNotEmpty()
                .matches(valuesAreNotEmpty);
    }

    @Test
    void set() {
        val value = random(new TypeToken<ImmutableSet<Integer>>() { },
                           Config.builder()
                                   .collNeverEmpty()
                                   .build());
        assertThat(value)
                .isNotNull()
                .isNotEmpty()
                .allMatch(Objects::nonNull);
    }
}
