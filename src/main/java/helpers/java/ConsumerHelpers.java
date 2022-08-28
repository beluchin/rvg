package helpers.java;

import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
public final class ConsumerHelpers {
    public static <T> Consumer<T> throwingConsumer() {
        return t -> { throw new RuntimeException(); };
    }
}
