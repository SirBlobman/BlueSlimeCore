package com.github.sirblobman.api.utility;

import java.util.Collection;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Validate {
    public static <O> @NotNull O notNull(@Nullable O value, @NotNull String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static @NotNull String notEmpty(@Nullable String value, @NotNull String message) {
        notNull(value, message);

        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static <O, C extends Collection<O>> @NotNull C notEmpty(@Nullable C value, @NotNull String message) {
        notNull(value, message);

        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static <K, V, M extends Map<K, V>> @NotNull M notEmpty(@Nullable M value, @NotNull String message) {
        notNull(value, message);

        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }
}
