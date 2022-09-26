package aq.rvg.config;

import aq.helpers.java.tuple.Tuple;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import static aq.helpers.java.ListHelpers.list;
import static aq.helpers.java.StreamHelpers.repeatedly;
import static aq.helpers.java.tuple.Tuple.tuple;
import static aq.rvg.Operational.random;
import static aq.rvg.Operational.randomInt;
import static aq.rvg.Operational.randomString;
import static aq.rvg.config.Operational.randomListSize;

@Value
public class Config {
    public static final Config DEFAULT = newDefaultConfig();
    public ImmutableList<Tuple<
            Predicate<TypeToken<?>>,
            BiFunction<TypeToken<?>, Config, ?>>> many_PredicateAndRandomFunction;

    public static Builder builder() {
        return new Builder();
    }

    public static Config empty() { return new Config(list()); }

    private static Config newDefaultConfig() {
        return Config.builder()
                .for_(int.class, (tt, config) -> randomInt())
                .for_(Integer.class, (tt, config) -> randomInt())
                .for_(String.class, (tt, config) -> randomString())

                // Guava collections
                .for_(ImmutableList.class,
                      (tt, config) -> {
                          val typeArg = tt.resolveType(ImmutableList.class.getTypeParameters()[0]);
                          val builder = ImmutableList.builder();
                          repeatedly(() -> random(typeArg, config),
                                     randomListSize(config))
                                  .forEach(builder::add);
                          return builder.build();
                      })
                .for_(ImmutableSet.class,
                      (tt, config) -> {
                          val typeArg = tt.resolveType(ImmutableSet.class.getTypeParameters()[0]);
                          val builder = ImmutableSet.builder();
                          repeatedly(() -> random(typeArg, config),
                                     randomListSize(config))
                                  .forEach(builder::add);
                          return builder.build();
                      })

                .build();
    }

    public static class Builder {
        private final List<Tuple<
                Predicate<TypeToken<?>>,
                BiFunction<TypeToken<?>, Config, ?>>> predicateAndRandomFunction
                = new ArrayList<>();

        public Builder add(Config c) {
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
