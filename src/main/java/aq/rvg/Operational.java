package aq.rvg;

import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;
import java.util.UUID;

import static aq.rvg.Functional.supplierOfRandom;
import static com.google.common.base.Preconditions.checkArgument;
import static java.time.temporal.ChronoUnit.DAYS;

@UtilityClass
public final class Operational {
    private static final LocalDate DATE_0 = LocalDate.of(1970, Month.JANUARY, 1);

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

    public static LocalDate randomLocalDate() {
        return randomLocalDate(DATE_0, DATE_0.plusYears(200));
    }

    public static LocalDate randomLocalDate(LocalDate startInclusive, LocalDate endExclusive) {
        checkArgument(startInclusive.isBefore(endExclusive));
        val days = DAYS.between(startInclusive, endExclusive);
        if (days > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Too many days between " + startInclusive + " and " + endExclusive);
        }
        return startInclusive.plusDays(randomInt(0, (int) days));
    }

    public static String randomString() {
        return UUID.randomUUID().toString();
    }

    static int getCollectionSize(Config config) {
        return config.optCollectionSize.orElse(randomInt(2, 6));
    }
}
