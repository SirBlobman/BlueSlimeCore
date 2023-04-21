package com.github.sirblobman.api.nbt.custom;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

public final class CustomNbtTypeBoolean implements CustomNbtType<Byte, Boolean> {
    @Override
    public @NotNull Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public @NotNull Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public @NotNull Byte toPrimitive(@NotNull Boolean complex, @NotNull CustomNbtContext context) {
        return (byte) (complex ? 1 : 0);
    }

    @Override
    public @NotNull Boolean fromPrimitive(@NotNull Byte primitive, @NotNull CustomNbtContext context) {
        return (primitive >= 1);
    }
}
