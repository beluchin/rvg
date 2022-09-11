package rvg;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import helpers.java.tuple.Tuple;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static helpers.java.ListHelpers.list;
import static helpers.java.tuple.Tuple.tuple;
import static rvg.Operational.randomInt;
import static rvg.Operational.randomString;

@Value
public class Config {
    static final Config DEFAULT = Config.builder()
            .for_(int.class, (tt, config) -> randomInt())
            .for_(Integer.class, (tt, config) -> randomInt())
            .for_(String.class, (tt, config) -> randomString())
            .build();
    public ImmutableList<Tuple<
            Predicate<TypeToken<?>>,
            BiFunction<TypeToken<?>, Config, ?>>> many_PredicateAndRandomFunction;

    public static Builder builder() {
        return new Builder();
    }

    public static Config empty() { return new Config(list()); }

    public static class Builder {
        private final List<Tuple<
                Predicate<TypeToken<?>>,
                BiFunction<TypeToken<?>, Config, ?>>> predicateAndRandomFunction
                = new ArrayList<>();

        public <E> Builder add(Config c) {
            predicateAndRandomFunction.addAll(c.many_PredicateAndRandomFunction);
            return this;
        }

        public Config build() {
            return new Config(ImmutableList.copyOf(predicateAndRandomFunction));
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
