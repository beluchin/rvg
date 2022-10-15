package aq.rvg;

import aq.helpers.java.tuple.Tuple;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static aq.helpers.SetHelpers.set;
import static aq.helpers.java.LambdaHelpers.sneakyThrows;
import static aq.helpers.java.ListHelpers.append;
import static aq.helpers.java.ListHelpers.last;
import static aq.helpers.java.ListHelpers.list;
import static aq.helpers.java.StreamHelpers.repeatedly;
import static aq.rvg.Operational.getCollectionSize;
import static aq.rvg.Operational.oneOf;
import static aq.rvg.Operational.random;
import static aq.rvg.Operational.randomBoolean;

@UtilityClass
final class Functional {
    static final Config DEFAULT = newDefaultConfig();

    static ImmutableList<TypeToken<?>> args(TypeToken<?> enclosingType,
                                            Constructor<?> c) {
        ensureNotLocalClass(enclosingType);

        val builder = ImmutableList.<TypeToken<?>>builder();
        for (val p : c.getParameters()) {
            builder.add(enclosingType.resolveType(p.getParameterizedType()));
        }
        return builder.build();
    }

    static Config reverse(Config config) {
        return new Config(config.optCollectionSize,
                          config.many_PredicateAndRandomFunction.reverse());
    }

    static <T> Supplier<T> supplierOfRandom(TypeToken<T> type, Config config) {
        //noinspection unchecked
        return (Supplier<T>) supplierOfRandomLastInPath(path(type), addDefaults(reverse(config)));
    }

    static <T> Supplier<T> supplierOfRandom(TypeToken<T> type) {
        //noinspection unchecked
        return (Supplier<T>) supplierOfRandomLastInPath(path(type), DEFAULT);
    }

    private static <T> BiFunction<TypeToken<?>, Config, T> __(Supplier<T> s) {
        return (tt, config) -> s.get();
    }

    private static Config addDefaults(Config orig) {
        return new Config(orig.optCollectionSize,
                          ImmutableList.<Tuple<
                                          Predicate<TypeToken<?>>,
                                          BiFunction<TypeToken<?>, Config, ?>>>builder()
                                  .addAll(orig.many_PredicateAndRandomFunction)
                                  .addAll(DEFAULT.many_PredicateAndRandomFunction)
                                  .build());
    }

    private static Constructor<?> constructor(TypeToken<?> tt) {
        val constructors = tt.getRawType().getConstructors();
        if (0 == constructors.length) {
            throw new IllegalArgumentException("No public constructors found for " + tt);
        }
        return constructors[0];
    }

    private static void ensureNoCycle(ImmutableList<TypeToken<?>> path) {
        if (set(path).size() != path.size()) {
            throw new IllegalArgumentException("Cycle detected in " + path);
        }
    }

    private static void ensureNotLocalClass(TypeToken<?> enclosingType) {
        if (enclosingType.getRawType().isLocalClass()) {
            throw new UnsupportedOperationException("Local classes are not supported");
        }
    }

    private static Config newDefaultConfig() {
        return Config.builder()
                .for_(String.class, __(Operational::randomString))

                .for_(int.class, __(Operational::randomInt))
                .for_(Integer.class, __(Operational::randomInt))

                .for_(double.class, __(Operational::randomDouble))
                .for_(Double.class, __(Operational::randomDouble))

                .for_(boolean.class, __(Operational::randomBoolean))
                .for_(Boolean.class, __(Operational::randomBoolean))

                .for_(LocalDate.class, __(Operational::randomLocalDate))

                .for_(tt -> Enum.class.isAssignableFrom(tt.getRawType()),
                      (tt, config) -> oneOf(tt.getRawType().getEnumConstants()))

                .for_(Optional.class,
                      (tt, config) -> {
                          val argTT = tt.resolveType(Optional.class.getTypeParameters()[0]);
                          return Optional.ofNullable(randomBoolean()
                                                             ? random(argTT, config)
                                                             : null);
                      })


                // Guava collections
                .for_(ImmutableList.class,
                      (tt, config) -> {
                          val typeArg = tt.resolveType(ImmutableList.class.getTypeParameters()[0]);
                          val builder = ImmutableList.builder();
                          repeatedly(() -> random(typeArg, config), getCollectionSize(config))
                                  .forEach(builder::add);
                          return builder.build();
                      })
                .for_(ImmutableSet.class,
                      (tt, config) -> {
                          val typeArg = tt.resolveType(ImmutableSet.class.getTypeParameters()[0]);
                          val builder = ImmutableSet.builder();
                          repeatedly(() -> random(typeArg, config), getCollectionSize(config))
                                  .forEach(builder::add);
                          return builder.build();
                      })

                .build();
    }

    private static Optional<Supplier<?>> optSupplierUsingOverrides(
            ImmutableList<TypeToken<?>> path,
            Config config) {
        val last = last(path);
        return config.many_PredicateAndRandomFunction.stream()
                .filter(predicateAndRandomFunction -> predicateAndRandomFunction._1.test(last))
                .<Supplier<?>>map(t -> () -> t._2.apply(last, config))
                .findFirst();
    }

    private static ImmutableList<TypeToken<?>> path(TypeToken<?> type) {
        return list(type);
    }

    private static Supplier<?> supplierOfRandomLastInPath(
            ImmutableList<TypeToken<?>> path,
            Config config) {
        return optSupplierUsingOverrides(path, config)
                .orElseGet(() -> {
                    ensureNoCycle(path);
                    return supplierUsingConstructor(path, config);
                });
    }

    private static Supplier<?> supplierUsingConstructor(
            ImmutableList<TypeToken<?>> path,
            Config config) {
        val type = last(path);
        val constructor = constructor(type);
        val args = args(type, constructor);

        return sneakyThrows(() -> constructor.newInstance(
                args.stream()
                        .map(arg -> supplierOfRandomLastInPath(append(path, arg), config))
                        .map(Supplier::get)
                        .toArray()));
    }
}
