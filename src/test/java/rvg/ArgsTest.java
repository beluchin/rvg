package rvg;

import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static helpers.java.ListHelpers.list;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static rvg.Functional.args;

class ArgsTest {
    @Disabled
    @Test
    void args_for_local_inner_class_constructor() {
        @Value
        class LocalInnerWithTypeArg<T> {
            T t;
        }

        val constructor = LocalInnerWithTypeArg.class.getConstructors()[0];
        assertThat(args(new TypeToken<LocalInnerWithTypeArg<String>>() {},
                        constructor))
                .isEqualTo(list(new TypeToken<ArgsTest>() {},
                                new TypeToken<String>() {}));
    }

    @Test
    void args_happy_path() {
        assertThat(args(new TypeToken<WithTypeArg<String>>() {},
                        WithTypeArg.class.getConstructors()[0]))
                .isEqualTo(list(new TypeToken<String>() {}));
    }

    @Test
    void args_local_inner_classes_are_unsupported() {
        @Value
        class LocalInnerWithTypeArg<T> {
            T t;
        }

        val constructor = LocalInnerWithTypeArg.class.getConstructors()[0];
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> args(new TypeToken<LocalInnerWithTypeArg<String>>() {},
                                       constructor));
    }

    @Value
    static class WithTypeArg<T> {
        T t;
    }
}