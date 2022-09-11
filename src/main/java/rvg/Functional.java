package rvg;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static helpers.java.ListHelpers.*;
import static helpers.java.MapHelpers.map;
import static helpers.java.MapHelpers.tryToGet;
import static helpers.java.OptionalHelpers.firstNonEmpty;

@SuppressWarnings("rawtypes")
@UtilityClass
final class Functional {
    static final Supplier<Integer> RANDOM_INT = Operational::randomInt;
    static final Supplier<String> RANDOM_STRING = Operational::randomString;
    private static final Map<Class<?>, Supplier<?>> BASIC_TYPE_TO_SUPPLIER = map(
            int.class, RANDOM_INT,
            Integer.class, RANDOM_INT,
            String.class, RANDOM_STRING);

    static ImmutableList<TypeToken<?>> args(TypeToken<?> enclosingType, Constructor<?> c) {
        ensureNotLocalClass(enclosingType);

        val builder = ImmutableList.<TypeToken<?>>builder();
        for (val p : c.getParameters()) {
            builder.add(enclosingType.resolveType(p.getParameterizedType()));
        }
        return builder.build();
    }

    static <T> Supplier<T> supplierOfRandom(TypeToken<T> type, Config config) {
        //noinspection unchecked
        return (Supplier<T>) supplierOfRandomLastInPath(path(type), config);
    }

    private static Constructor<?> constructor(TypeToken<?> tt) {
        val constructors = tt.getRawType().getConstructors();
        if (0 == constructors.length) {
            throw new IllegalArgumentException("No public constructors found for " + tt);
        }
        return constructors[0];
    }

    private static void ensureNotLocalClass(TypeToken<?> enclosingType) {
        if (enclosingType.getRawType().isLocalClass()) {
            throw new UnsupportedOperationException("Local classes are not supported");
        }
    }

    private static Optional<Supplier<?>> optSupplierIfBasicType(List<TypeToken<?>> path) {
        return tryToGet(BASIC_TYPE_TO_SUPPLIER, last(path).getRawType());
    }

    private static Optional<Supplier<?>> optSupplierUsingOverrides(
            ImmutableList<TypeToken<?>> path, Config config) {
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
            ImmutableList<TypeToken<?>> path, Config config) {
        return firstNonEmpty(
                optSupplierUsingOverrides(path, config),
                () -> optSupplierIfBasicType(path))
                .orElseGet(() -> supplierUsingConstructor(path, config));
    }

    private static Supplier supplierUsingConstructor(
            ImmutableList<TypeToken<?>> path,
            Config config) {
        val type = last(path);
        val constructor = constructor(type);
        val args = args(type, constructor);

        return () -> {
            val argSuppliers = args.stream()
                    .map(arg -> supplierOfRandomLastInPath(append(path, arg), config))
                    .map(Supplier::get)
                    .collect(Collectors.toList());
            try {
                return constructor.newInstance(argSuppliers.toArray());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}
