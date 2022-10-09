package aq.rvg;

import aq.rvg.config.Config;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static aq.rvg.Functional.supplierOfRandom;
import static com.google.common.base.Preconditions.checkArgument;

@UtilityClass
public final class Operational {
    @SafeVarargs
    public static <T> T oneOf(T first, T... rest) {
        return rest.length == 0 ? first : rest[randomInt(0, rest.length)];
    }

    public static <T> T random(TypeToken<T> type, Config config) {
        return supplierOfRandom(type, config).get();
    }

    public static <T> T random(TypeToken<T> type) {
        return supplierOfRandom(type).get();
    }

    public static boolean randomBoolean() {
        return Math.random() > 0.5;
    }

    public static int randomInt() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    public static int randomInt(int including, int excluding) {
        checkArgument(including >= 0 && including < excluding);
        return including + randomInt() % (excluding - including);
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }
}
