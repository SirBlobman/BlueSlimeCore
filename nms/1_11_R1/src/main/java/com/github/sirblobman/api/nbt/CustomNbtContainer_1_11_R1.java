package com.github.sirblobman.api.nbt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import net.minecraft.server.v1_11_R1.NBTBase;
import net.minecraft.server.v1_11_R1.NBTTagCompound;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("NullableProblems")
public final class CustomNbtContainer_1_11_R1 implements CustomNbtContainer {
    private final Map<String, NBTBase> customDataTags;
    private final CustomNbtTypeRegistry_1_11_R1 registry;
    private final CustomNbtContext_1_11_R1 adapterContext;

    public CustomNbtContainer_1_11_R1(Map<String, NBTBase> customTags, CustomNbtTypeRegistry_1_11_R1 registry) {
        this(registry);
        this.customDataTags.putAll(customTags);
    }

    public CustomNbtContainer_1_11_R1(CustomNbtTypeRegistry_1_11_R1 registry) {
        this.customDataTags = new HashMap<>();
        this.registry = registry;
        this.adapterContext = new CustomNbtContext_1_11_R1(this.registry);
    }

    @Override
    public <T, Z> void set(String key, CustomNbtType<T, Z> type, Z value) {
        Validate.notNull(key, "key must not be null!");
        Validate.notNull(type, "type must not be null!");
        Validate.notNull(value, "value must not be null!");

        Class<T> primitiveType = type.getPrimitiveType();
        T primitiveValue = type.toPrimitive(value, this.adapterContext);
        NBTBase wrap = this.registry.wrap(primitiveType, primitiveValue);
        this.customDataTags.put(key, wrap);
    }

    @Override
    public <T, Z> boolean has(String key, CustomNbtType<T, Z> type) {
        Validate.notNull(key, "key must not be null!");
        Validate.notNull(type, "type must not be null!");

        NBTBase value = this.customDataTags.get(key);
        if (value == null) {
            return false;
        }

        Class<T> primitiveType = type.getPrimitiveType();
        return this.registry.isInstanceOf(primitiveType, value);
    }

    @Override
    public <T, Z> Z get(String key, CustomNbtType<T, Z> type) {
        Validate.notNull(key, "key must not be null!");
        Validate.notNull(type, "type must not be null!");

        NBTBase value = this.customDataTags.get(key);
        if (value == null) {
            return null;
        }

        Class<T> primitiveType = type.getPrimitiveType();
        T extract = this.registry.extract(primitiveType, value);
        return type.fromPrimitive(extract, this.adapterContext);
    }

    @Override
    public <T, Z> Z getOrDefault(String key, CustomNbtType<T, Z> type, Z defaultValue) {
        Z value = get(key, type);
        return (value != null ? value : defaultValue);
    }

    @Override
    public void remove(String key) {
        Validate.notNull(key, "key must not be null!");
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
    public CustomNbtContext getContext() {
        return this.adapterContext;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomNbtContainer_1_11_R1)) {
            return false;
        } else {
            Map<String, NBTBase> myRawMap = this.getRaw();
            Map<String, NBTBase> theirRawMap = ((CustomNbtContainer_1_11_R1) obj).getRaw();
            return Objects.equals(myRawMap, theirRawMap);
        }
    }

    public NBTTagCompound toTagCompound() {
        NBTTagCompound tag = new NBTTagCompound();
        Set<Entry<String, NBTBase>> entrySet = this.customDataTags.entrySet();

        for (Entry<String, NBTBase> entry : entrySet) {
            String key = entry.getKey();
            NBTBase value = entry.getValue();
            tag.set(key, value);
        }

        return tag;
    }

    public void put(String key, NBTBase base) {
        this.customDataTags.put(key, base);
    }

    public void putAll(Map<String, NBTBase> map) {
        this.customDataTags.putAll(map);
    }

    @SuppressWarnings("unchecked")
    public void putAll(NBTTagCompound compound) {
        Set<String> keySet = (Set<String>) compound.c();
        for (String key : keySet) {
            NBTBase value = compound.get(key);
            this.customDataTags.put(key, value);
        }
    }

    public Map<String, NBTBase> getRaw() {
        return this.customDataTags;
    }

    public CustomNbtTypeRegistry_1_11_R1 getDataTagTypeRegistry() {
        return this.registry;
    }

    public int hashCode() {
        int hashCode = 3;
        hashCode += this.customDataTags.hashCode();
        return hashCode;
    }

//    public Map<String, Object> serialize() {
//        return (Map)CraftNBTTagConfigSerializer.serialize(this.toTagCompound());
//    }
}
