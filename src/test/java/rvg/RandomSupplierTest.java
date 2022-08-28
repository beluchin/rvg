package rvg;

import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static rvg.Functional.*;

@SuppressWarnings("unchecked")
final class RandomSupplierTest {
    @Test
    void _1_basic_type() {
        assertThat(supplierOfRandomLast(path(new TypeToken<Integer>() { }), Config.empty()))
                .isEqualTo(RANDOM_INT);
    }

    @Test
    void _2_user_defined() {
        val supplier = supplierOfRandomLast(path(new TypeToken<WithoutTypeArgs>() { }),
                                            Config.empty());
        val value = (WithoutTypeArgs) supplier.get();
        assertThat(value.i).isNotZero();
    }

    @Test
    void _3_user_defined_with_type_args() {
        val supplier = supplierOfRandomLast(path(new TypeToken<WithTypeArgs<Integer>>() { }),
                                            Config.empty());
        val value = (WithTypeArgs<Integer>) supplier.get();
        assertThat(value.t).isNotZero();
    }

    @Test
    void _4_arbitrary_nesting() {
        val supplier = supplierOfRandomLast(
                path(new TypeToken<WithTypeArgs<WithTypeArgs<Integer>>>() { }),
                Config.empty());
        val value = (WithTypeArgs<WithTypeArgs<Integer>>) supplier.get();
        assertThat(value.t.t).isNotZero();
    }

    @Test
    void _5_multiple_constructor_args() {
        val supplier = supplierOfRandomLast(
                path(new TypeToken<WithMultipleConstructorArgs<Integer>>() { }),
                Config.empty());
        val value = (WithMultipleConstructorArgs<Integer>) supplier.get();

        assertThat(value.i).isNotZero();
        assertThat(value.t).isNotZero();
    }

    @Value
    static class WithMultipleConstructorArgs<T> {
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
}
