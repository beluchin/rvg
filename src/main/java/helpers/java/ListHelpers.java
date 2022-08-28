package helpers.java;

import com.google.common.collect.ImmutableList;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public final class ListHelpers {
    public static <T> T last(List<T> list) {
        return list.get(list.size() - 1);
    }

    @SafeVarargs
    public static <T> ImmutableList<T> list(T... ts) {
        return ImmutableList.copyOf(ts);
    }

    public static <T> ImmutableList<T> append(ImmutableList<T> ts, T t) {
        return ImmutableList.<T>builder()
                .addAll(ts)
                .add(t)
                .build();
    }
}
