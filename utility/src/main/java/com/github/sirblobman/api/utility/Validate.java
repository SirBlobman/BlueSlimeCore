package com.github.sirblobman.api.utility;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Validate {
    @NotNull
    public static <O> O notNull(@Nullable O value, String message) {
        if(value == null) throw new IllegalArgumentException(message);
        return value;
    }

    @NotNull
    public static String notEmpty(@Nullable String value, String message) {
        notNull(value, message);
        if(value.isEmpty()) throw new IllegalArgumentException(message);
        return value;
    }

    @NotNull
    public static <O, C extends Collection<O>> C notEmpty(@Nullable C value, String message) {
        notNull(value, message);
        if(value.isEmpty()) throw new IllegalArgumentException(message);
        return value;
    }
}
