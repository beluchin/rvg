package rvg;

import com.google.common.reflect.TypeToken;
import helpers.java.either.Either;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static rvg.Functional.supplierOfRandom;
import static rvg.Operational.randomBoolean;

final class SupplierOfRandomTest {
    @Test
    void _1_basic_type() {
        assertThat(supplierOfRandom(new TypeToken<Integer>() { }, Config.empty()).get())
                .isNotZero();
        assertThat(supplierOfRandom(new TypeToken<String>() { }, Config.empty()).get())
                .isNotNull();
    }

    @Test
    void _2_user_defined() {
        val value = supplierOfRandom(new TypeToken<WithoutTypeArgs>() { },
                                     Config.empty())
                .get();

        assertThat(value.i).isNotZero();
    }

    @Test
    void _3_user_defined_with_type_args() {
        val value = supplierOfRandom(new TypeToken<WithTypeArgs<Integer>>() { },
                                     Config.empty())
                .get();

        assertThat(value.t).isNotZero();
    }

    @Test
    void _4_arbitrary_nesting() {
        val value = supplierOfRandom(new TypeToken<WithTypeArgs<WithTypeArgs<Integer>>>() { },
                                     Config.empty())
                .get();

        assertThat(value.t.t).isNotZero();
    }

    @Test
    void _5_multiple_constructor_args() {
        val value = supplierOfRandom(new TypeToken<WithConstructorWithMultipleArgs<Integer>>() { },
                                     Config.empty())
                .get();

        assertThat(value.i).isNotZero();
        assertThat(value.t).isNotZero();
    }

    @Value
    static class WithConstructorWithMultipleArgs<T> {
        int i;
        T t;
    }

    @Value
    static class WithTypeArgs<T> {
        T t;
    }

    @Value
    static class WithoutTypeArgs {
        int i;
    }

    @Nested
    class ConfigTest {
        private final Config config = Config.builder()
                .for_(tt -> tt.getRawType().equals(Either.class),
                      (tt, c) -> {
                          val typeArg0 = tt.resolveType(Either.class.getTypeParameters()[0]);
                          val typeArg1 = tt.resolveType(Either.class.getTypeParameters()[1]);
                          return randomBoolean()
                                  ? Either.first(Operational.random(typeArg0, c))
                                  : Either.second(Operational.random(typeArg1, c));
                      })
                .build();

        @Test
        void _1_special_constructor() {
            val value = supplierOfRandom(new TypeToken<Either<Integer, String>>() { },
                                         config)
                    .get();

            value.apply(i -> assertThat(i).isNotZero(),
                        s -> assertThat(s).isNotNull());
        }

        @Test
        void _2_special_constructor_nested() {
            val value = supplierOfRandom(new TypeToken<WithTypeArgs<Either<Integer, String>>>() { },
                                         config)
                    .get();

            value.t.apply(i -> assertThat(i).isNotZero(),
                          s -> assertThat(s).isNotNull());
        }
    }
}
