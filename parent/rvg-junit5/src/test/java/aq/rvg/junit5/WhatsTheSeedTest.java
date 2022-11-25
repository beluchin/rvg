package aq.rvg.junit5;

import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.randomInt;
import static aq.rvg.Operational.setSeed;
import static org.assertj.core.api.Assertions.assertThat;

//@ExtendWith(WhatsTheSeedExtension.class)
final class WhatsTheSeedTest {
//    @RepeatedTest(100)
    @Test
    void repeated() {
        setSeed(1667471142416L); // -2146043208
//        assertThat(randomInt()).isPositive();
        assertThat(randomInt()).isEqualTo(-2146043208);
    }
}
