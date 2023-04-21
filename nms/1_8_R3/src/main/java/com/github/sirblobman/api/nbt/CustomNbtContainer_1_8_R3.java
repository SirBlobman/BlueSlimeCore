package com.github.sirblobman.api.nbt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public final class CustomNbtContainer_1_8_R3 implements CustomNbtContainer {
    private final Map<String, NBTBase> customDataTags;
    private final CustomNbtTypeRegistry_1_8_R3 registry;
    private final CustomNbtContext_1_8_R3 adapterContext;

    public CustomNbtContainer_1_8_R3(@NotNull Map<String, NBTBase> customTags,
                                     @NotNull CustomNbtTypeRegistry_1_8_R3 registry) {
        this(registry);
        this.customDataTags.putAll(customTags);
    }

    public CustomNbtContainer_1_8_R3(@NotNull CustomNbtTypeRegistry_1_8_R3 registry) {
        this.customDataTags = new HashMap<>();
        this.registry = registry;
        this.adapterContext = new CustomNbtContext_1_8_R3(this.registry);
    }

    @Override
    public <T, Z> void set(@NotNull String key, @NotNull CustomNbtType<T, Z> type, @NotNull Z value) {
        Class<T> primitiveType = type.getPrimitiveType();
        T primitiveValue = type.toPrimitive(value, this.adapterContext);
        NBTBase wrap = this.registry.wrap(primitiveType, primitiveValue);
        this.customDataTags.put(key, wrap);
    }

    @Override
    public <T, Z> boolean has(@NotNull String key, @NotNull CustomNbtType<T, Z> type) {
        NBTBase value = this.customDataTags.get(key);
        if (value == null) {
            return false;
        }

        Class<T> primitiveType = type.getPrimitiveType();
        return this.registry.isInstanceOf(primitiveType, value);
    }

    @Override
    public <T, Z> @Nullable Z get(@NotNull String key, @NotNull CustomNbtType<T, Z> type) {
        NBTBase value = this.customDataTags.get(key);
        if (value == null) {
            return null;
        }

        Class<T> primitiveType = type.getPrimitiveType();
        T extract = this.registry.extract(primitiveType, value);
        return type.fromPrimitive(extract, this.adapterContext);
    }

    @Override
    public <T, Z> @Nullable Z getOrDefault(@NotNull String key, @NotNull CustomNbtType<T, Z> type,
                                           @Nullable Z defaultValue) {
        Z value = get(key, type);
        return (value != null ? value : defaultValue);
    }

    @Override
    public void remove(@NotNull String key) {
        this.customDataTags.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return this.customDataTags.isEmpty();
    }

    @Override
    public @NotNull Set<String> getKeys() {
        Set<String> keySet = this.customDataTags.keySet();
        return Collections.unmodifiableSet(keySet);
    }

    @Override
    public @NotNull CustomNbtContext getContext() {
        return this.adapterContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomNbtContainer_1_8_R3)) {
            return false;
        } else {
            Map<String, NBTBase> myRawMap = this.getRaw();
            Map<String, NBTBase> theirRawMap = ((CustomNbtContainer_1_8_R3) obj).getRaw();
            return Objects.equals(myRawMap, theirRawMap);
        }
    }

    public @NotNull NBTTagCompound toTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        Set<Entry<String, NBTBase>> entrySet = this.customDataTags.entrySet();

        for (Entry<String, NBTBase> entry : entrySet) {
            String key = entry.getKey();
            NBTBase value = entry.getValue();
            tag.set(key, value);
        }

        return tag;
    }

    public void put(@NotNull String key, @NotNull NBTBase base) {
        this.customDataTags.put(key, base);
    }

    public @NotNull Map<String, NBTBase> getRaw() {
        return this.customDataTags;
    }

    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customDataTags.hashCode();
        return hashCode;
    }
}
