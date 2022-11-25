package aq.helpers.java;

import lombok.experimental.UtilityClass;

import java.util.function.Supplier;

/**
 * @see <a href="https://stackoverflow.com/a/27644392/614800">https://stackoverflow.com/a/27644392/614800</a>
 */
@UtilityClass
public final class LambdaHelpers {
    public static <T1, E extends Throwable> Supplier<T1> sneakyThrows(
            SupplierWithException<T1, E> supplier) {
        return () -> {
            try {
                return supplier.get();
            } catch (Throwable e) {
                throwAsUnchecked(e);
            }
            throw new AssertionError("Unreachable");
        };
    }

    private static <E extends Throwable> void throwAsUnchecked(Throwable e) throws E {
        //noinspection unchecked
        throw (E) e;
    }
}
