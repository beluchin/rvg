package rvg;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import static rvg.Functional.path;
import static rvg.Functional.supplierOfRandomLast;

@UtilityClass
public final class Operational {
    public static <T> T random(TypeToken<T> type, Config config) {
        //noinspection unchecked
        return (T) supplierOfRandomLast(path(type), config).get();
    }

    public static int randomInt() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

}
