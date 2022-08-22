package com.github.sirblobman.api.nbt;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class CustomNbtTypePrimitive<T> implements CustomNbtType<T, T> {
    private final Class<T> primitiveType;

    CustomNbtTypePrimitive(Class<T> primitiveType) {
        this.primitiveType = Validate.notNull(primitiveType, "primitiveType must not be null!");
    }

    @NotNull
    @Override
    public Class<T> getPrimitiveType() {
        return this.primitiveType;
    }

    @NotNull
    @Override
    public Class<T> getComplexType() {
        return this.primitiveType;
    }

    @NotNull
    @Override
    public T toPrimitive(@NotNull T complex, @NotNull CustomNbtContext context) {
        return complex;
    }

    @NotNull
    @Override
    public T fromPrimitive(@NotNull T primitive, @NotNull CustomNbtContext context) {
        return primitive;
    }
}
