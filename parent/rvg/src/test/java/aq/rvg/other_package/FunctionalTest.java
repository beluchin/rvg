package aq.rvg.other_package;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThat;

class FunctionalTest {
    @Test
    void t() {
        assertThat(random(new TypeToken<PackageRecord>() { })).isNotNull();
    }
}
