package aq.rvg;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.val;
import org.junit.jupiter.api.Test;

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
    void set() {
        val value = random(new TypeToken<ImmutableSet<Integer>>() { });
        assertThat(value)
                .isNotNull()
                .isNotEmpty()
                .allMatch(i -> i != 0);
    }
}
