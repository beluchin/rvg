package aq.rvg.junit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import static aq.rvg.Operational.randomInt;
import static aq.rvg.junit4.WhatsTheSeedRule.whatsTheSeedRule;
import static org.assertj.core.api.Assertions.assertThat;

public class WhatsTheSeedTest {
//    @Rule public TestRule r = whatsTheSeedRule();

    @Test
    public void to_run_repeatedly_until_failure() {
//        assertThat(randomInt()).isPositive();
    }
}
