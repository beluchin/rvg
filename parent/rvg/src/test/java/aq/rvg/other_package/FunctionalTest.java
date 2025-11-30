package aq.rvg.other_package;

import com.google.common.reflect.TypeToken;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;
import static org.assertj.core.api.Assertions.assertThat;

class FunctionalTest {
    @Test
    void t() {
        assertThat(random(new TypeToken<PackageRecord>() { })).isNotNull();
    }

    @Test
    void instancio() {
        assertThat(Instancio.create(PackageRecord.class)).isNotNull();
    }
}
