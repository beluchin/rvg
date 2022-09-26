package aq.rvg;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

final class CycleTest {
    @Test
    void depends_on_itself() {
        //noinspection unused
        class C {
            C c;
        }
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> random(new TypeToken<C>() { }));
    }
}
