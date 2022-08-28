package helpers.java;

import lombok.experimental.UtilityClass;

import java.util.function.Function;

@UtilityClass
public final class FunctionHelpers {
    public static <K, V> Function<K, V> throwingFunction() {
        return k -> { throw new RuntimeException(); };
    }
}
