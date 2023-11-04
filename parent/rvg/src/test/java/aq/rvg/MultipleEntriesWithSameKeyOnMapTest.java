package aq.rvg;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;

final class MultipleEntriesWithSameKeyOnMapTest {
    @Test
    void test() {
        random(new TypeToken<ImmutableMap<AnEnum, Object>>() { });
    }

    enum AnEnum {PV01, RATE}
}
