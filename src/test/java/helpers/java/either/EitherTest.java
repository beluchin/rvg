package helpers.java.either;

import helpers.java.MutableReference;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static helpers.java.ConsumerHelpers.throwingConsumer;
import static helpers.java.FunctionHelpers.throwingFunction;
import static org.assertj.core.api.Assertions.assertThat;

final class EitherTest {
    @Test
    void equality() {
        val lhs = Either.<Integer, String>first(42);
        val rhs = Either.<Integer, Integer>first(42);
        //noinspection AssertBetweenInconvertibleTypes
        assertThat(lhs).isEqualTo(rhs);
    }

    @Test
    void left_apply() {
        val ref = MutableReference.<String>empty();

        Either.first(1).apply(
                i -> ref.set("42"),
                throwingConsumer());

        assertThat(ref.get()).isEqualTo("42");
    }

    @Test
    void left_map() {
        assertThat(
                Either.first(1).map(
                        (Function<Integer, String>) i -> "42",
                        throwingFunction()))
                .isEqualTo("42");
    }

    @Test
    void right_apply() {
        val ref = MutableReference.<String>empty();

        Either.second(1).apply(
                throwingConsumer(),
                i -> ref.set("42"));

        assertThat(ref.get()).isEqualTo("42");
    }

    @Test
    void right_map() {
        assertThat(
                Either.second(1).map(
                        throwingFunction(),
                        (Function<Integer, String>) i -> "42"))
                .isEqualTo("42");
    }
}
