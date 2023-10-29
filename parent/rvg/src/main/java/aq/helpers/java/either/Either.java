package aq.helpers.java.either;

import lombok.Value;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

@Value
public class Either<T0, T1> {
    OneOf oneOf;

    private Either(int index, Object value) {
        oneOf = new OneOf(index, value);
    }

    public void accept(Consumer<? super T0> lFunc, Consumer<? super T1> rFunc) {
        oneOf.apply(lFunc, rFunc);
    }

    public <T> T map(
            Function<? super T0, ? extends T> lFunc,
            Function<? super T1, ? extends T> rFunc) {
        //noinspection unchecked
        return (T) oneOf.map(lFunc, rFunc);
    }

    public static <T0, T1> Either<T0, T1> first(T0 value) {
        checkNotNull(value);
        return new Either<>(0, value);
    }

    public static <T0, T1> Either<T0, T1> second(T1 value) {
        checkNotNull(value);
        return new Either<>(1, value);
    }
}