package aq.rvg;

import aq.rvg.config.Config;
import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

final class CycleTest {
    @Test
    void _1_depends_on_itself() {
        //noinspection unused
        class C {
            C c;
        }
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> random(new TypeToken<C>() { }));
    }

    @Test
    void _2_two_classes_depend_on_each_other() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> random(new TypeToken<C1>() { }));
    }

    @Test
    void _3_address_cycle_with_config() {
        val config = Config.builder()
                .for_(C2.class, (tt, c) -> new C2(null))
                .build();

        assertThat(random(new TypeToken<C1>() { }, config)).isNotNull();
    }

    @Value
    private static class C1 {
        C2 c2;
    }

    @Value
    private static class C2 {
        C1 c1;
    }
}
