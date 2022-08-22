package com.github.sirblobman.api.nbt;

import com.github.sirblobman.api.nbt.custom.CustomNbtTypeBoolean;
import com.github.sirblobman.api.nbt.custom.CustomNbtTypeUUID;

public final class CustomNbtTypes {
    /*
        Java primitive types.
     */
    public static CustomNbtType<Byte, Byte> BYTE;
    public static CustomNbtType<Short, Short> SHORT;
    public static CustomNbtType<Integer, Integer> INTEGER;
    public static CustomNbtType<Long, Long> LONG;
    public static CustomNbtType<Float, Float> FLOAT;
    public static CustomNbtType<Double, Double> DOUBLE;

    /*
        String.
     */
    public static CustomNbtType<String, String> STRING;

    /*
        Primitive Arrays.
     */
    public static CustomNbtType<byte[], byte[]> BYTE_ARRAY;
    public static CustomNbtType<int[], int[]> INTEGER_ARRAY;
    public static CustomNbtType<long[], long[]> LONG_ARRAY;

    /*
        Complex Arrays.
     */
    public static CustomNbtType<CustomNbtContainer[], CustomNbtContainer[]> TAG_CONTAINER_ARRAY;

    /*
        Nested CustomNbtContainer.
     */
    public static CustomNbtType<CustomNbtContainer, CustomNbtContainer> TAG_CONTAINER;

    /*
        Real Custom NBT Types
     */
    public static CustomNbtTypeUUID UUID;
    public static CustomNbtTypeBoolean BOOLEAN;

    static {
        BYTE = new CustomNbtTypePrimitive<>(Byte.class);
        SHORT = new CustomNbtTypePrimitive<>(Short.class);
        INTEGER = new CustomNbtTypePrimitive<>(Integer.class);
        LONG = new CustomNbtTypePrimitive<>(Long.class);
        FLOAT = new CustomNbtTypePrimitive<>(Float.class);
        DOUBLE = new CustomNbtTypePrimitive<>(Double.class);

        STRING = new CustomNbtTypePrimitive<>(String.class);

        BYTE_ARRAY = new CustomNbtTypePrimitive<>(byte[].class);
        INTEGER_ARRAY = new CustomNbtTypePrimitive<>(int[].class);
        LONG_ARRAY = new CustomNbtTypePrimitive<>(long[].class);

        TAG_CONTAINER_ARRAY = new CustomNbtTypePrimitive<>(CustomNbtContainer[].class);

        TAG_CONTAINER = new CustomNbtTypePrimitive<>(CustomNbtContainer.class);

        UUID = new CustomNbtTypeUUID();
        BOOLEAN = new CustomNbtTypeBoolean();
    }
}
