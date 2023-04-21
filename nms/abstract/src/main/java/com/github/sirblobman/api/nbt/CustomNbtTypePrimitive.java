package com.github.sirblobman.api.nbt;

import org.jetbrains.annotations.NotNull;

public final class CustomNbtTypePrimitive<T> implements CustomNbtType<T, T> {
    private final Class<T> primitiveType;

    CustomNbtTypePrimitive(@NotNull Class<T> primitiveType) {
        this.primitiveType = primitiveType;
    }

    @Override
    public @NotNull Class<T> getPrimitiveType() {
        return this.primitiveType;
    }

    @Override
    public @NotNull Class<T> getComplexType() {
        return this.primitiveType;
    }

    @Override
    public @NotNull T toPrimitive(@NotNull T complex, @NotNull CustomNbtContext context) {
        return complex;
    }

    @Override
    public @NotNull T fromPrimitive(@NotNull T primitive, @NotNull CustomNbtContext context) {
        return primitive;
    }
}
