package aq.rvg;

import aq.helpers.java.either.Either;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static aq.rvg.Operational.random;
import static aq.rvg.Operational.randomBoolean;
import static org.assertj.core.api.Assertions.assertThat;

final class RandomTest {
    @Test
    void _1_basic_type() {
        assertThat(random(new TypeToken<Integer>() { })).isNotZero();
    }

    @Test
    void _2_user_defined() {
        val value = random(new TypeToken<WithoutTypeArgs>() { });
        assertThat(value.i).isNotZero();
    }

    @Test
    void _3_user_defined_with_type_args() {
        val value = random(new TypeToken<WithTypeArgs<Integer>>() { });
        assertThat(value.t).isNotZero();
    }

    @Test
    void _4_arbitrary_nesting() {
        val value = random(new TypeToken<WithTypeArgs<WithTypeArgs<Integer>>>() { });
        assertThat(value.t.t).isNotZero();
    }

    @Test
    void _5_multiple_constructor_args() {
        val value = random(new TypeToken<WithConstructorWithMultipleArgs<Integer>>() { });
        assertThat(value.i).isNotZero();
        assertThat(value.t).isNotZero();
    }

    @ParameterizedTest
    @MethodSource
    void out_of_the_box(TypeToken<?> tt) {
        assertThat(random(tt)).isNotNull();
    }

    private static Stream<Arguments> out_of_the_box() {
        return Stream.of(new TypeToken<AnEnum>() { },
                         new TypeToken<Optional<?>>() { },
                         new TypeToken<String>() { },
                         new TypeToken<LocalDate>() {},
                         new TypeToken<Object>() {})
                .map(Arguments::arguments);
    }

    enum AnEnum { @SuppressWarnings("unused") A }

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
    class BugsTest {
        @Test
        void overriden_random_int() {
            val config = Config.builder()
                    .for_(Integer.class, (tt, c) -> 42)
                    .build();

            val value = random(new TypeToken<Integer>() { }, config);

            assertThat(value).isEqualTo(42);
        }
    }

    @Nested
    class ConfigTest {
        private final Config config = Config.builder()
                .for_(tt -> tt.getRawType().equals(Either.class),
                      (tt, config) -> {
                          val typeArg0 = tt.resolveType(Either.class.getTypeParameters()[0]);
                          val typeArg1 = tt.resolveType(Either.class.getTypeParameters()[1]);
                          return randomBoolean()
                                  ? Either.first(random(typeArg0, config))
                                  : Either.second(random(typeArg1, config));
                      })
                .build();

        @Test
        void _1_special_constructor() {
            val value = random(new TypeToken<Either<Integer, String>>() { },
                               config);
            value.accept(i -> assertThat(i).isNotZero(),
                        s -> assertThat(s).isNotNull());
        }

        @Test
        void _2_special_constructor_nested() {
            val value = random(new TypeToken<WithTypeArgs<Either<Integer, String>>>() { },
                               config);
            value.t.accept(i -> assertThat(i).isNotZero(),
                          s -> assertThat(s).isNotNull());
        }

        @Test
        void _3_last_override_wins() {
            val config = Config.builder()
                    .for_(Integer.class, (tt, c) -> 0)
                    .for_(Integer.class, (tt, c) -> 42)
                    .build();

            val value = random(new TypeToken<Integer>() { }, config);

            assertThat(value).isEqualTo(42);
        }

        @Test
        void _4_collection_size() {
            val config = Config.builder()
                    .collectionSize(5)
                    .build();

            assertThat(random(new TypeToken<WithTypeArgs<ImmutableList<String>>>() { },
                              config).t)
                    .hasSize(5);
        }
    }
}
