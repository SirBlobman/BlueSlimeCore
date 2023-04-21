package com.github.sirblobman.api.nbt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

import org.jetbrains.annotations.NotNull;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagByte;
import net.minecraft.server.v1_8_R3.NBTTagByteArray;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagDouble;
import net.minecraft.server.v1_8_R3.NBTTagFloat;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.NBTTagIntArray;
import net.minecraft.server.v1_8_R3.NBTTagLong;
import net.minecraft.server.v1_8_R3.NBTTagShort;
import net.minecraft.server.v1_8_R3.NBTTagString;

import com.google.common.primitives.Primitives;
import org.apache.commons.lang3.Validate;

public final class CustomNbtTypeRegistry_1_8_R3 {
    private final Function<Class<?>, Adapter_1_8_R3<?, ?>> createAdapter;
    private final Map<Class<?>, Adapter_1_8_R3<?, ?>> adapters;

    public CustomNbtTypeRegistry_1_8_R3() {
        this.createAdapter = this::createAdapter;
        this.adapters = new HashMap<>();
    }

    private <T> @NotNull Adapter_1_8_R3<?, ?> createAdapter(@NotNull Class<T> type) {
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
            return createAdapter(CustomNbtContainer_1_8_R3.class, NBTTagCompound.class,
                    CustomNbtContainer_1_8_R3::toTagCompound, this::tagToContainer);
        } else {
            String simpleName = type.getSimpleName();
            String message = ("Could not find a valid CustomNbtTagAdapter implementation for the requested type "
                    + simpleName);
            throw new IllegalArgumentException(message);
        }
    }

    private @NotNull CustomNbtContainer_1_8_R3 tagToContainer(@NotNull NBTTagCompound tag) {
        CustomNbtContainer_1_8_R3 container = new CustomNbtContainer_1_8_R3(this);
        Set<String> keySet = tag.c();
        for (String key : keySet) {
            NBTBase value = tag.get(key);
            container.put(key, value);
        }

        return container;
    }

    private @NotNull NBTTagString longArrayToNBT(long[] longArray) {
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

    private long @NotNull [] nbtToLongArray(@NotNull NBTTagString nbt) {
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

    private <T, Z extends NBTBase> @NotNull Adapter_1_8_R3<T, Z> createAdapter(@NotNull Class<T> primitiveType,
                                                                               @NotNull Class<Z> nbtBaseType,
                                                                               @NotNull Function<T, Z> builder,
                                                                               @NotNull Function<Z, T> extractor) {
        return new Adapter_1_8_R3<>(primitiveType, nbtBaseType, builder, extractor);
    }

    public <T> @NotNull NBTBase wrap(@NotNull Class<T> type, @NotNull T value) {
        return this.adapters.computeIfAbsent(type, this.createAdapter).build(value);
    }

    public <T> boolean isInstanceOf(@NotNull Class<T> type, @NotNull NBTBase base) {
        return this.adapters.computeIfAbsent(type, this.createAdapter).isInstance(base);
    }

    public <T> @NotNull T extract(@NotNull Class<T> type, @NotNull NBTBase tag)
            throws ClassCastException, IllegalArgumentException {
        Adapter_1_8_R3<?, ?> adapter = this.adapters.computeIfAbsent(type, this.createAdapter);
        Validate.isTrue(adapter.isInstance(tag), "`The found tag instance cannot store %s as it is a %s",
                type.getSimpleName(), tag.getClass().getSimpleName());
        Object foundValue = adapter.extract(tag);
        Validate.isInstanceOf(type, foundValue, "The found object is of the type %s. Expected type %s",
                foundValue.getClass().getSimpleName(), type.getSimpleName());
        return type.cast(foundValue);
    }
}
