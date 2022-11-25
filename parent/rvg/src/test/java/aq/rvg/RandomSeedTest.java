package aq.rvg;

import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.randomInt;
import static aq.rvg.Operational.setSeed;
import static org.assertj.core.api.Assertions.assertThat;

final class RandomSeedTest {
    @Test
    void int_() {
        setSeed(42);
        assertThat(randomInt()).isEqualTo(-1170105035);
    }
}
