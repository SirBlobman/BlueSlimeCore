package com.github.sirblobman.api.nbt.custom;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;

import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

public final class CustomNbtTypeUUID implements CustomNbtType<byte[], UUID> {
    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(UUID complex, @NotNull CustomNbtContext context) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(complex.getMostSignificantBits());
        byteBuffer.putLong(complex.getLeastSignificantBits());
        return byteBuffer.array();
    }

    @Override
    public @NotNull UUID fromPrimitive(byte @NotNull [] primitive, @NotNull CustomNbtContext context) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(primitive);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
