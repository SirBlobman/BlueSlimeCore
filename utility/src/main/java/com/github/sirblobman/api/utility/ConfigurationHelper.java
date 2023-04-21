package com.github.sirblobman.api.utility;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ConfigurationHelper {
    @Contract("_, _, !null -> !null")
    public static <E extends Enum<E>> E parseEnum(@NotNull Class<E> enumClass, @NotNull String value,
                                                  @Nullable E defaultValue) {
        String uppercaseValue = value.toUpperCase(Locale.US);
        String fixedValue = uppercaseValue.replace(' ', '_').replace('-', '_');

        E[] enumValues = enumClass.getEnumConstants();
        for (E enumValue : enumValues) {
            String enumName = enumValue.name();
            if (enumName.equals(fixedValue)) {
                return enumValue;
            }
        }

        return defaultValue;
    }

    public static <E extends Enum<E>> @NotNull Set<E> parseEnums(@NotNull Collection<String> items,
                                                                 @NotNull Class<E> enumClass) {
        if (items.isEmpty()) {
            return EnumSet.noneOf(enumClass);
        }

        if (items.contains("*")) {
            return EnumSet.allOf(enumClass);
        }

        E[] enumValues = enumClass.getEnumConstants();
        Set<E> enumSet = EnumSet.noneOf(enumClass);

        for (E enumValue : enumValues) {
            String enumName = enumValue.name();
            if (items.contains(enumName)) {
                enumSet.add(enumValue);
            }
        }

        return enumSet;
    }

    public static <E extends Enum<E>> @NotNull Set<String> getEnumNames(@NotNull Class<E> enumClass) {
        E[] enumArray = enumClass.getEnumConstants();
        Set<String> enumNameSet = new HashSet<>();

        for (E enumValue : enumArray) {
            String enumName = enumValue.name();
            enumNameSet.add(enumName);
        }

        return Collections.unmodifiableSet(enumNameSet);
    }
}
