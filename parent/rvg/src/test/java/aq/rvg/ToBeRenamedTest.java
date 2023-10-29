package aq.rvg;

import aq.helpers.java.either.Either;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import lombok.Value;
import lombok.val;
import org.junit.jupiter.api.Test;

import static aq.helpers.java.MapHelpers.map;
import static aq.rvg.Operational.random;
import static aq.rvg.Operational.randomBoolean;
import static aq.rvg.Operational.randomDouble;
import static aq.rvg.ToBeRenamedTest.Measure.PV01;
import static org.assertj.core.api.Assertions.assertThat;

final class ToBeRenamedTest {
    @Test
    void t() {
        val config = Config.builder()
                .for_(Either.class,
                      (tt, c) -> {
                          val typeArg0 = tt.resolveType(Either.class.getTypeParameters()[0]);
                          val typeArg1 = tt.resolveType(Either.class.getTypeParameters()[1]);
                          return randomBoolean()
                                  ? Either.first(random(typeArg0, c))
                                  : Either.second(random(typeArg1, c));
                      })
                .for_(tt -> tt.equals(new TypeToken<ImmutableMap<Measure, Double>>() { }),
                      (tt, c) -> map(PV01, randomDouble()))
                .build();

        val update = random(new TypeToken<Update>() { }, config);

        update.successOrFailure.accept(
                success -> assertThat(success.measureToValue).containsOnlyKeys(PV01),
                failure -> { });
    }

    enum Measure {PV01, RATE}

    @Value
    static class Update {
        public Either<Success, Failure> successOrFailure;

        @Value
        static class Failure {
            public ImmutableSet<String> errors;
        }

        @Value
        static class Success {
            public ImmutableMap<Measure, Double> measureToValue;
        }
    }
}
