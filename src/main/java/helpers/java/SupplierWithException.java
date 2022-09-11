package helpers.java;

@FunctionalInterface
public interface SupplierWithException<T, E extends Throwable> {
    T get() throws E;
}
