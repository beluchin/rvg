package aq.rvg;

import aq.helpers.java.either.Either;
import aq.rvg.config.Config;
import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static aq.rvg.Operational.random;
import static aq.rvg.Operational.randomBoolean;
import static org.assertj.core.api.Assertions.assertThat;

final class RandomTest {
    @Test
    void _1_basic_type() {
        assertThat(random(new TypeToken<Integer>() { })).isNotZero();
        assertThat(random(new TypeToken<String>() { })).isNotNull();
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
                    .for_(int.class, (tt, c) -> 42)
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
            value.apply(i -> assertThat(i).isNotZero(),
                        s -> assertThat(s).isNotNull());
        }

        @Test
        void _2_special_constructor_nested() {
            val value = random(new TypeToken<WithTypeArgs<Either<Integer, String>>>() { },
                               config);
            value.t.apply(i -> assertThat(i).isNotZero(),
                          s -> assertThat(s).isNotNull());
        }

        @Test
        void _3_last_override_wins() {
            val config = Config.builder()
                    .for_(int.class, (tt, c) -> 0)
                    .for_(int.class, (tt, c) -> 42)
                    .build();

            val value = random(new TypeToken<Integer>() { }, config);

            assertThat(value).isEqualTo(42);
        }
    }
}
