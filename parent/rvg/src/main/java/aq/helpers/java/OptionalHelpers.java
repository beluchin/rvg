package aq.helpers.java;

import lombok.experimental.UtilityClass;
import lombok.val;

import java.util.Optional;
import java.util.function.Supplier;

@UtilityClass
public final class OptionalHelpers {
    @SafeVarargs
    public static <T> Optional<T> firstNonEmpty(Optional<T>... options) {
        for (val o : options) {
            if (o.isPresent()) {
                return o;
            }
        }
        return Optional.empty();
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @SafeVarargs
    public static <T> Optional<T> firstNonEmpty(
            Optional<T> first,
            Supplier<Optional<T>>... suppliers) {
        if (first.isPresent()) {
            return first;
        }
        for (val s : suppliers) {
            val o = s.get();
            if (o.isPresent()) {
                return o;
            }
        }
        return Optional.empty();
    }
}
