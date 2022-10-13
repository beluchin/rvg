package aq.rvg;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.UUID;

import static aq.rvg.Functional.supplierOfRandom;
import static com.google.common.base.Preconditions.checkArgument;

@UtilityClass
public final class Operational {
    @SafeVarargs
    public static <T> T oneOf(T... ts) {
        if (ts.length == 0) {
            throw new IllegalArgumentException("Must have at least one element");
        }
        return ts[randomInt(0, ts.length)];
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

    public static double randomDouble() {
        return new Random().nextDouble();
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

    static int getCollectionSize(Config config) {
        return config.optCollectionSize.orElse(randomInt(2, 6));
    }
}
