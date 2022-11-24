package aq.rvg;

import aq.helpers.java.tuple.Tuple;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

import static aq.helpers.java.tuple.Tuple.tuple;
import static aq.rvg.Functional.supplierOfRandom;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.abs;
import static java.time.temporal.ChronoUnit.DAYS;

@UtilityClass
public final class Operational {
    private static final LocalDate DATE_0 = LocalDate.of(1970, Month.JANUARY, 1);
    private static final ThreadLocal<Tuple<Random, Long>> threadLocalRandomAndSeed = ThreadLocal.withInitial(
            () -> {
                val seed = System.currentTimeMillis();
                return tuple(new Random(seed), seed);
            });

    public static long getSeed() {
        return threadLocalRandomAndSeed.get()._2;
    }

    public static void setSeed(long seed) {
        getRandom().setSeed(seed);
    }

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
        return getRandom().nextBoolean();
    }

    public static double randomDouble() {
        return getRandom().nextDouble();
    }

    public static int randomInt() {
        return getRandom().nextInt();
    }

    public static int randomInt(int including, int excluding) {
        checkArgument(including >= 0 && including < excluding);
        return including + abs(randomInt() % (excluding - including));
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
        return getRandom().nextLong() + "";
    }

    static int getCollectionSize(Config config) {
        val s = config.optCollectionSize.orElse(randomInt(2, 6));
        if (s <=0 ) {
            throw new IllegalArgumentException("Collection size must be positive");
        }
        return s;
    }

    private static Random getRandom() {
        return threadLocalRandomAndSeed.get()._1;
    }
}
