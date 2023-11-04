package aq.rvg;

import aq.helpers.java.tuple.Tuple;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static aq.helpers.java.tuple.Tuple.tuple;
import static com.google.common.collect.ImmutableList.copyOf;
import static lombok.AccessLevel.NONE;

@Value
@Builder
@Getter(NONE)
public class Config {
    public boolean collMayBeEmpty; // default: true
    public ImmutableList<Tuple<
            Predicate<TypeToken<?>>,
            BiFunction<TypeToken<?>, Config, ?>>> many_PredicateAndRandomFunction;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<Tuple<
                Predicate<TypeToken<?>>,
                BiFunction<TypeToken<?>, Config, ?>>> predicateAndRandomFunction
                = new ArrayList<>();
        private boolean collMayBeEmpty = true;

        public Builder add(Config c) {
            predicateAndRandomFunction.addAll(c.many_PredicateAndRandomFunction);
            return this;
        }

        public Config build() {
            return new Config(collMayBeEmpty, copyOf(predicateAndRandomFunction));
        }

        public Builder collMayBeEmpty() {
            collMayBeEmpty = true;
            return this;
        }

        public Builder collNeverEmpty() {
            collMayBeEmpty = false;
            return this;
        }

        public <T> Builder for_(Class<T> c, BiFunction<TypeToken<?>, Config, T> randomFunction) {
            return for_(tt -> tt.getRawType().equals(c), randomFunction);
        }

        public Builder for_(Predicate<TypeToken<?>> predicate,
                            BiFunction<TypeToken<?>, Config, ?> randomFn) {
            predicateAndRandomFunction.add(tuple(predicate, randomFn));
            return this;
        }
    }
}
