package aq.helpers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class SetHelpers {
    public static <T> ImmutableSet<T> set(ImmutableList<T> list) {
        return ImmutableSet.copyOf(list);
    }
}
