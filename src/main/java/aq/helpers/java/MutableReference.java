package aq.helpers.java;

import javax.annotation.Nullable;

public final class MutableReference<T> {
    private @Nullable T value;

    @Nullable
    public T get() {
        return value;
    }

    public void set(T t) {
        value = t;
    }

    public static <T> MutableReference<T> empty() {
        return new MutableReference<>();
    }
}
