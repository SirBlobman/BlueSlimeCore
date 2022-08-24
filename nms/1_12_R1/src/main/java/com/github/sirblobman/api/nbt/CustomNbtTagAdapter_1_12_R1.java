package com.github.sirblobman.api.nbt;

import java.util.function.Function;

import net.minecraft.server.v1_12_R1.NBTBase;

import org.apache.commons.lang3.Validate;

public final class CustomNbtTagAdapter_1_12_R1<T, Z extends NBTBase> {
    private final Function<T, Z> builder;
    private final Function<Z, T> extractor;
    private final Class<T> primitiveType;
    private final Class<Z> nbtBaseType;

    public CustomNbtTagAdapter_1_12_R1(Class<T> primitiveType, Class<Z> nbtBaseType, Function<T, Z> builder,
                                       Function<Z, T> extractor) {
        this.primitiveType = primitiveType;
        this.nbtBaseType = nbtBaseType;
        this.builder = builder;
        this.extractor = extractor;
    }

    T extract(NBTBase base) {
        Validate.isInstanceOf(this.nbtBaseType, base, "The provided NBTBase was of the type %s. " +
                "Expected type %s", base.getClass().getSimpleName(), this.nbtBaseType.getSimpleName());
        return this.extractor.apply(this.nbtBaseType.cast(base));
    }

    Z build(Object value) {
        Validate.isInstanceOf(this.primitiveType, value, "The provided value was of the type %s. " +
                "Expected type %s", value.getClass().getSimpleName(), this.primitiveType.getSimpleName());
        return this.builder.apply(this.primitiveType.cast(value));
    }

    boolean isInstance(NBTBase base) {
        return this.nbtBaseType.isInstance(base);
    }
}
