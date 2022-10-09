package aq.rvg.config;

import lombok.experimental.UtilityClass;

import static aq.rvg.Operational.oneOf;

@UtilityClass
public final class Operational {
    public static int getCollectionSize(Config config) {
        return oneOf(0, 2);
    }
}
