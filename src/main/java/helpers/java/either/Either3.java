package helpers.java.either;

import lombok.Value;

import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

@Value
public class Either3<T0, T1, T2> {
    OneOf oneOf;

    private Either3(int index, Object value) {
        oneOf = new OneOf(index, value);
    }

    public void apply(Consumer<? super T0> t0Consumer,
                      Consumer<? super T1> t1Consumer,
                      Consumer<? super T2> t2Consumer) {
        oneOf.apply(t0Consumer, t1Consumer, t2Consumer);
    }

    public <T> T map(Function<? super T0, ? extends T> t0Func,
                     Function<? super T1, ? extends T> t1Func,
                     Function<? super T2, ? extends T> t2Func) {
        //noinspection unchecked
        return (T) oneOf.map(t0Func, t1Func, t2Func);
    }

    public static <T0, T1, T2> Either3<T0, T1, T2> first(T0 value) {
        checkNotNull(value);
        return new Either3<>(0, value);
    }

    public static <T0, T1, T2> Either3<T0, T1, T2> second(T1 value) {
        checkNotNull(value);
        return new Either3<>(1, value);
    }

    public static <T0, T1, T2> Either3<T0, T1, T2> third(T2 value) {
        checkNotNull(value);
        return new Either3<>(2, value);
    }
}
