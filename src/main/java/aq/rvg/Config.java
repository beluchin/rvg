package aq.rvg;

import aq.helpers.java.tuple.Tuple;
import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static aq.helpers.java.tuple.Tuple.tuple;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.ImmutableList.copyOf;
import static lombok.AccessLevel.NONE;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@Value
@Builder
@Getter(NONE)
public class Config {
    public OptionalInt optCollectionSize;
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
        private OptionalInt optCollectionSize = OptionalInt.empty();

        public Builder add(Config c) {
            predicateAndRandomFunction.addAll(c.many_PredicateAndRandomFunction);
            return this;
        }

        public Config build() {
            return new Config(optCollectionSize, copyOf(predicateAndRandomFunction));
        }

        public Builder collectionSize(int i) {
            checkArgument(i > 0, "Collection size must be positive");
            optCollectionSize = OptionalInt.of(i);
            return this;
        }

        public Builder for_(Class<?> c, BiFunction<TypeToken<?>, Config, ?> randomFunction) {
            return for_(tt -> tt.getRawType().equals(c), randomFunction);
        }

        public Builder for_(Predicate<TypeToken<?>> predicate,
                            BiFunction<TypeToken<?>, Config, ?> randomFn) {
            predicateAndRandomFunction.add(tuple(predicate, randomFn));
            return this;
        }
    }
}
