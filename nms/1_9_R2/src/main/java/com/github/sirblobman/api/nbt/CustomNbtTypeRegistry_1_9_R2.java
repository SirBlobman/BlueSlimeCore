package com.github.sirblobman.api.nbt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import net.minecraft.server.v1_9_R2.NBTBase;
import net.minecraft.server.v1_9_R2.NBTTagByte;
import net.minecraft.server.v1_9_R2.NBTTagByteArray;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagDouble;
import net.minecraft.server.v1_9_R2.NBTTagFloat;
import net.minecraft.server.v1_9_R2.NBTTagInt;
import net.minecraft.server.v1_9_R2.NBTTagIntArray;
import net.minecraft.server.v1_9_R2.NBTTagLong;
import net.minecraft.server.v1_9_R2.NBTTagShort;
import net.minecraft.server.v1_9_R2.NBTTagString;

import com.google.common.primitives.Primitives;
import org.apache.commons.lang3.Validate;

public final class CustomNbtTypeRegistry_1_9_R2 {
    private final Function<Class<?>, CustomNbtTagAdapter_1_9_R2<?, ?>> CREATE_ADAPTER = this::createAdapter;
    private final Map<Class<?>, CustomNbtTagAdapter_1_9_R2<?, ?>> adapters = new HashMap<>();

    private <T> CustomNbtTagAdapter_1_9_R2<?, ?> createAdapter(Class<T> type) {
        if (!Primitives.isWrapperType(type)) {
            type = Primitives.wrap(type);
        }

        if (Objects.equals(Byte.class, type)) {
            return this.createAdapter(Byte.class, NBTTagByte.class, NBTTagByte::new, NBTTagByte::f);
        } else if (Objects.equals(Short.class, type)) {
            return this.createAdapter(Short.class, NBTTagShort.class, NBTTagShort::new, NBTTagShort::e);
        } else if (Objects.equals(Integer.class, type)) {
            return this.createAdapter(Integer.class, NBTTagInt.class, NBTTagInt::new, NBTTagInt::d);
        } else if (Objects.equals(Long.class, type)) {
            return this.createAdapter(Long.class, NBTTagLong.class, NBTTagLong::new, NBTTagLong::c);
        } else if (Objects.equals(Float.class, type)) {
            return this.createAdapter(Float.class, NBTTagFloat.class, NBTTagFloat::new, NBTTagFloat::h);
        } else if (Objects.equals(Double.class, type)) {
            return this.createAdapter(Double.class, NBTTagDouble.class, NBTTagDouble::new, NBTTagDouble::g);
        } else if (Objects.equals(String.class, type)) {
            return this.createAdapter(String.class, NBTTagString.class, NBTTagString::new, NBTTagString::a_);
        } else if (Objects.equals(byte[].class, type)) {
            return this.createAdapter(byte[].class, NBTTagByteArray.class,
                    (array) -> new NBTTagByteArray(Arrays.copyOf(array, array.length)),
                    (n) -> Arrays.copyOf(n.c(), n.c().length));
        } else if (Objects.equals(int[].class, type)) {
            return this.createAdapter(int[].class, NBTTagIntArray.class,
                    (array) -> new NBTTagIntArray(Arrays.copyOf(array, array.length)),
                    (n) -> Arrays.copyOf(n.c(), n.c().length));
        } else if (Objects.equals(long[].class, type)) {
            return createAdapter(long[].class, NBTTagString.class, this::longArrayToNBT, this::nbtToLongArray);
        } else if (Objects.equals(CustomNbtContainer.class, type)) {
            return createAdapter(CustomNbtContainer_1_9_R2.class, NBTTagCompound.class,
                    CustomNbtContainer_1_9_R2::toTagCompound, this::tagToContainer);
        } else {
            throw new IllegalArgumentException("Could not find a valid CustomNbtTagAdapter implementation for the " +
                    "requested type " + type.getSimpleName());
        }
    }

    private CustomNbtContainer_1_9_R2 tagToContainer(NBTTagCompound tag) {
        CustomNbtContainer_1_9_R2 container = new CustomNbtContainer_1_9_R2(this);
        Set<String> keySet = tag.c();
        for (String key : keySet) {
            NBTBase value = tag.get(key);
            container.put(key, value);
        }

        return container;
    }

    private NBTTagString longArrayToNBT(long[] longArray) {
        StringBuilder builder = new StringBuilder();
        for (long part : longArray) {
            if (builder.length() > 0) {
                builder.append(';');
            }

            builder.append(part);
        }

        String value = builder.toString();
        return new NBTTagString(value);
    }

    private long[] nbtToLongArray(NBTTagString nbt) {
        String string = nbt.a_();
        String[] split = string.split(Pattern.quote(";"));
        int splitLength = split.length;

        try {
            long[] longArray = new long[splitLength];
            for (int i = 0; i < split.length; i++) {
                String part = split[i];
                longArray[i] = Long.parseLong(part);
            }

            return longArray;
        } catch (NumberFormatException ex) {
            return new long[0];
        }
    }

    private <T, Z extends NBTBase> CustomNbtTagAdapter_1_9_R2<T, Z> createAdapter(Class<T> primitiveType,
                                                                                  Class<Z> nbtBaseType,
                                                                                  Function<T, Z> builder,
                                                                                  Function<Z, T> extractor) {
        return new CustomNbtTagAdapter_1_9_R2<>(primitiveType, nbtBaseType, builder, extractor);
    }

    public <T> NBTBase wrap(Class<T> type, T value) {
        return this.adapters.computeIfAbsent(type, this.CREATE_ADAPTER).build(value);
    }

    public <T> boolean isInstanceOf(Class<T> type, NBTBase base) {
        return this.adapters.computeIfAbsent(type, this.CREATE_ADAPTER).isInstance(base);
    }

    public <T> T extract(Class<T> type, NBTBase tag) throws ClassCastException, IllegalArgumentException {
        CustomNbtTagAdapter_1_9_R2<?, ?> adapter = this.adapters.computeIfAbsent(type, this.CREATE_ADAPTER);
        Validate.isTrue(adapter.isInstance(tag), "`The found tag instance cannot store %s as it is a %s",
                type.getSimpleName(), tag.getClass().getSimpleName());
        Object foundValue = adapter.extract(tag);
        Validate.isInstanceOf(type, foundValue, "The found object is of the type %s. Expected type %s",
                foundValue.getClass().getSimpleName(), type.getSimpleName());
        return type.cast(foundValue);
    }
}
