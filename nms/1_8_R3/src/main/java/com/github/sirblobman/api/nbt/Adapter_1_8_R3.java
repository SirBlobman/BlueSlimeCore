package com.github.sirblobman.api.nbt;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.v1_8_R3.NBTBase;

import org.apache.commons.lang3.Validate;

public final class Adapter_1_8_R3<T, Z extends NBTBase> {
    private final Function<T, Z> builder;
    private final Function<Z, T> extractor;
    private final Class<T> primitiveType;
    private final Class<Z> complexType;

    public Adapter_1_8_R3(@NotNull Class<T> primitiveType, @NotNull Class<Z> complexType,
                          @NotNull Function<T, Z> builder, Function<Z, T> extractor) {
        this.primitiveType = primitiveType;
        this.complexType = complexType;
        this.builder = builder;
        this.extractor = extractor;
    }

    @NotNull T extract(@NotNull NBTBase base) {
        Class<? extends NBTBase> baseClass = base.getClass();
        String baseClassName = baseClass.getSimpleName();
        String complexTypeName = this.complexType.getSimpleName();

        String message = "The provided NBT Base was one of the type %s. Expected type %s.";
        Validate.isInstanceOf(this.complexType, base, message, baseClassName, complexTypeName);

        Z castedValue = this.complexType.cast(base);
        return this.extractor.apply(castedValue);
    }

    @NotNull Z build(@NotNull Object value) {
        Class<?> valueClass = value.getClass();
        String valueClassName = valueClass.getSimpleName();
        String primitiveClassName = this.primitiveType.getSimpleName();

        String message = "The provided value was of the type %s. Expected type %s.";
        Validate.isInstanceOf(this.primitiveType, value, message, valueClassName, primitiveClassName);

        T castedValue = this.primitiveType.cast(value);
        return this.builder.apply(castedValue);
    }

    boolean isInstance(@NotNull NBTBase base) {
        return this.complexType.isInstance(base);
    }
}
