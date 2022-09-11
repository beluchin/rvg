package rvg;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.experimental.UtilityClass;
import lombok.val;

import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static helpers.java.ListHelpers.append;
import static helpers.java.ListHelpers.last;
import static helpers.java.ListHelpers.list;

@SuppressWarnings("rawtypes")
@UtilityClass
final class Functional {
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
        return (Supplier<T>) supplierOfRandomLastInPath(path(type), withDefaults(config));
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
        return optSupplierUsingOverrides(path, config)
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

    private static Config withDefaults(Config orig) {
        return Config.builder()
                .add(Config.DEFAULT)
                .add(orig)
                .build();
    }
}
