package com.github.sirblobman.api.nbt;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.v1_12_R1.NBTBase;

import org.apache.commons.lang3.Validate;

public final class Adapter_1_12_R1<T, Z extends NBTBase> {
    private final Function<T, Z> builder;
    private final Function<Z, T> extractor;
    private final Class<T> primitiveType;
    private final Class<Z> complexType;

    public Adapter_1_12_R1(@NotNull Class<T> primitiveType, @NotNull Class<Z> complexType,
                           @NotNull Function<T, Z> builder, @NotNull Function<Z, T> extractor) {
        this.primitiveType = primitiveType;
        this.complexType = complexType;
        this.builder = builder;
        this.extractor = extractor;
    }

    @NotNull T extract(@NotNull NBTBase base) {
        Validate.isInstanceOf(this.complexType, base, "The provided NBTBase was of the type %s. " +
                "Expected type %s", base.getClass().getSimpleName(), this.complexType.getSimpleName());
        return this.extractor.apply(this.complexType.cast(base));
    }

    @NotNull Z build(@NotNull Object value) {
        Validate.isInstanceOf(this.primitiveType, value, "The provided value was of the type %s. " +
                "Expected type %s", value.getClass().getSimpleName(), this.primitiveType.getSimpleName());
        return this.builder.apply(this.primitiveType.cast(value));
    }

    boolean isInstance(@NotNull NBTBase base) {
        return this.complexType.isInstance(base);
    }
}
