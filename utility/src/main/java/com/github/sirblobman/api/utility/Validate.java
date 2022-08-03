package com.github.sirblobman.api.utility;

import java.util.Collection;
import java.util.Map;

public final class Validate {
    public static <O> O notNull(O value, String message) {
        if (value == null) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static String notEmpty(String value, String message) {
        notNull(value, message);

        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static <O, C extends Collection<O>> C notEmpty(C value, String message) {
        notNull(value, message);

        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }

    public static <K, V, M extends Map<K, V>> M notEmpty(M value, String message) {
        notNull(value, message);

        if (value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }

        return value;
    }
}
