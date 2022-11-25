package aq.helpers.java.either;

import lombok.Value;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "rawtypes"})
@Value
class OneOf {
    int index;
    Object value;

    OneOf(int index, Object value) {
        this.index = index;
        this.value = value;
    }

    void apply(Consumer... fs) {
        fs[index].accept(value);
    }

    Object map(Function... fs) {
        return fs[index].apply(value);
    }
}
