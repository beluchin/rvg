package aq.rvg.config;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Functional {
    public static Config reverse(Config config) {
        return new Config(config.many_PredicateAndRandomFunction.reverse());
    }
}
