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

@SuppressWarnings("rawtypes")
@UtilityClass
final class Functional {
    static final Supplier<Integer> RANDOM_INT = Operational::randomInt;
    private static final Map<Class, Supplier> BASIC_TYPE_TO_SUPPLIER = map(
            int.class, RANDOM_INT,
            Integer.class, RANDOM_INT
                                                                          );

    @SuppressWarnings("rawtypes")
    static ImmutableList<TypeToken> args(TypeToken<?> enclosingType, Constructor<?> c) {
        ensureNotLocalClass(enclosingType);

        val builder = ImmutableList.<TypeToken>builder();
        for (val p : c.getParameters()) {
            builder.add(enclosingType.resolveType(p.getParameterizedType()));
        }
        return builder.build();
    }

    static ImmutableList<TypeToken> path(TypeToken type) {
        return list(type);
    }

    static Supplier supplierOfRandomLast(ImmutableList<TypeToken> path, Config config) {
        return matchLastAsBasicType(path).orElseGet(
                () -> supplierOfRandomLastFromConstructor(path, config));
    }

    private static Constructor<?> constructor(TypeToken tt) {
        return tt.getRawType().getConstructors()[0];
    }

    private static void ensureNotLocalClass(TypeToken<?> enclosingType) {
        if (enclosingType.getRawType().isLocalClass()) {
            throw new UnsupportedOperationException("Local classes are not supported");
        }
    }

    private static Optional<Supplier> matchLastAsBasicType(List<TypeToken> path) {
        return tryToGet(BASIC_TYPE_TO_SUPPLIER, last(path).getRawType());
    }

    private static Supplier supplierOfRandomLastFromConstructor(
            ImmutableList<TypeToken> path,
            Config config) {
        val type = last(path);
        val constructor = constructor(type);
        val args = args(type, constructor);

        return () -> {
            val argSuppliers = args.stream()
                    .map(arg -> supplierOfRandomLast(append(path, arg), config))
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
