package com.SirBlobman.api.utility;

public final class Validate {
    public static <O> O notNull(O value, String message) {
        if(value == null) throw new IllegalArgumentException(message);
        return value;
    }

    public static String notEmpty(String value, String message) {
        notNull(value, message);
        if(value.isEmpty()) throw new IllegalArgumentException(message);
        return value;
    }
}
