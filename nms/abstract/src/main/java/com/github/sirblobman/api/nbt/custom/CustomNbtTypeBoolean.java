package com.github.sirblobman.api.nbt.custom;

import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

@SuppressWarnings("NullableProblems")
public final class CustomNbtTypeBoolean implements CustomNbtType<Byte, Boolean> {
    @Override
    public Class<Byte> getPrimitiveType() {
        return Byte.class;
    }

    @Override
    public Class<Boolean> getComplexType() {
        return Boolean.class;
    }

    @Override
    public Byte toPrimitive(Boolean complex, CustomNbtContext context) {
        return (byte) (complex ? 1 : 0);
    }

    @Override
    public Boolean fromPrimitive(Byte primitive, CustomNbtContext context) {
        return (primitive == 1);
    }
}
