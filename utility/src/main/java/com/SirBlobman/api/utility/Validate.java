package com.SirBlobman.api.utility;

import org.jetbrains.annotations.Nullable;

public final class Validate {
    public static <O> O notNull(@Nullable O value, String message) {
        if(value == null) throw new IllegalArgumentException(message);
        return value;
    }

    public static String notEmpty(@Nullable String value, String message) {
        notNull(value, message);
        if(value.isEmpty()) throw new IllegalArgumentException(message);
        return value;
    }
}
