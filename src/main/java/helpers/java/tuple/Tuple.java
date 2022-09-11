package helpers.java.tuple;

import lombok.Value;

import java.util.Map;

@Value
public class Tuple<T1, T2> implements Map.Entry<T1, T2> {
    public T1 _1;
    public T2 _2;

    @Override
    public T1 getKey() {
        return _1;
    }

    @Override
    public T2 getValue() {
        return _2;
    }

    @Override
    public T2 setValue(T2 value) {
        throw new UnsupportedOperationException();
    }

    public static <T1, T2> Tuple<T1, T2> tuple(T1 t1, T2 t2) {
        return new Tuple<>(t1, t2);
    }
}
