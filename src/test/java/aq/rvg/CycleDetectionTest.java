package aq.rvg;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@UtilityClass
final class CycleDetectionTest {
    @Test
    void depends_on_itself() {
        class C {
            C c;
        }
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> random(new TypeToken<C>() { }));
    }
}
