package aq.rvg;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.util.UUID;

import static aq.rvg.Functional.supplierOfRandom;

@UtilityClass
public final class Operational {
    public static <T> T random(TypeToken<T> type, Config config) {
        return supplierOfRandom(type, config).get();
    }

    public static boolean randomBoolean() {
        return Math.random() > 0.5;
    }

    public static int randomInt() {
        return (int) (Math.random() * Integer.MAX_VALUE);
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

}
