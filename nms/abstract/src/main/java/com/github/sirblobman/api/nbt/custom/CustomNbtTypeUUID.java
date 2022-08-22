package com.github.sirblobman.api.nbt.custom;

import java.nio.ByteBuffer;
import java.util.UUID;

import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

@SuppressWarnings("NullableProblems")
public final class CustomNbtTypeUUID implements CustomNbtType<byte[], UUID> {
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<UUID> getComplexType() {
        return UUID.class;
    }

    @Override
    public byte[] toPrimitive(UUID complex, CustomNbtContext context) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(complex.getMostSignificantBits());
        byteBuffer.putLong(complex.getLeastSignificantBits());
        return byteBuffer.array();
    }

    @Override
    public UUID fromPrimitive(byte[] primitive, CustomNbtContext context) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(primitive);
        long mostSignificantBits = byteBuffer.getLong();
        long leastSignificantBits = byteBuffer.getLong();
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
}
