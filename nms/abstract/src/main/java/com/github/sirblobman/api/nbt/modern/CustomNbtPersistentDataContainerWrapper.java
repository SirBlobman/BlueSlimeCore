package com.github.sirblobman.api.nbt.modern;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.sirblobman.api.nbt.modern.PersistentDataConverter.convertContext;
import static com.github.sirblobman.api.nbt.modern.PersistentDataConverter.convertType;

public final class CustomNbtPersistentDataContainerWrapper implements CustomNbtContainer {
    private final Plugin plugin;
    private final PersistentDataContainer container;

    public CustomNbtPersistentDataContainerWrapper(Plugin plugin, PersistentDataContainer container) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.container = Validate.notNull(container, "container must not be null!");
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public <T, Z> void set(@NotNull String key, @NotNull CustomNbtType<T, Z> type, @NotNull Z value) {
        Plugin plugin = getPlugin();
        NamespacedKey realKey = getKey(key);
        PersistentDataContainer container = getContainer();
        PersistentDataType<T, Z> realType = convertType(plugin, type);
        container.set(realKey, realType, value);
    }

    @Override
    public <T, Z> boolean has(@NotNull String key, @NotNull CustomNbtType<T, Z> type) {
        Plugin plugin = getPlugin();
        NamespacedKey realKey = getKey(key);
        PersistentDataContainer container = getContainer();
        PersistentDataType<T, Z> realType = convertType(plugin, type);
        return container.has(realKey, realType);
    }

    @Override
    public <T, Z> @Nullable Z get(@NotNull String key, @NotNull CustomNbtType<T, Z> type) {
        Plugin plugin = getPlugin();
        NamespacedKey realKey = getKey(key);
        PersistentDataContainer container = getContainer();
        PersistentDataType<T, Z> realType = convertType(plugin, type);
        return container.get(realKey, realType);
    }

    @Override
    public <T, Z> @NotNull Z getOrDefault(@NotNull String key, @NotNull CustomNbtType<T, Z> type, @NotNull Z defaultValue) {
        Plugin plugin = getPlugin();
        NamespacedKey realKey = getKey(key);
        PersistentDataContainer container = getContainer();
        PersistentDataType<T, Z> realType = convertType(plugin, type);
        return container.getOrDefault(realKey, realType, defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Set<String> getKeys() {
        try {
            PersistentDataContainer container = getContainer();
            Class<?> containerClass = container.getClass();
            Method method_getKeys = containerClass.getMethod("getKeys");
            Set<NamespacedKey> realKeys = (Set<NamespacedKey>) method_getKeys.invoke(container);
            return realKeys.parallelStream().map(NamespacedKey::getKey).collect(Collectors.toSet());
        } catch (ReflectiveOperationException | ClassCastException ignored) {
            return Collections.emptySet();
        }
    }

    @Override
    public void remove(@NotNull String key) {
        PersistentDataContainer container = getContainer();
        NamespacedKey realKey = getKey(key);
        container.remove(realKey);
    }

    @Override
    public boolean isEmpty() {
        PersistentDataContainer container = getContainer();
        return container.isEmpty();
    }

    @Override
    public @NotNull CustomNbtContext getContext() {
        Plugin plugin = getPlugin();
        PersistentDataContainer container = getContainer();
        return convertContext(plugin, container.getAdapterContext());
    }

    private PersistentDataContainer getContainer() {
        return this.container;
    }

    private NamespacedKey getKey(String key) {
        Plugin plugin = getPlugin();
        return new NamespacedKey(plugin, key);
    }
}
