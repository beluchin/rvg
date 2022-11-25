package aq.helpers.java;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;
import java.util.stream.Stream;

@UtilityClass
public final class StreamHelpers {
    /**
     * @see <a href="https://clojuredocs.org/clojure.core/repeatedly">https://clojuredocs.org/clojure.core/repeatedly</a>
     */
    public static <T> Stream<T> repeatedly(Supplier<T> supplier, int times) {
        return Stream.generate(supplier).limit(times);
    }
}
