package com.github.sirblobman.api.nbt.modern;

import org.jetbrains.annotations.NotNull;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

import static com.github.sirblobman.api.nbt.modern.PersistentDataConverter.convertContext;

public final class CustomNbtPersistentDataTypeWrapper<T, Z> implements PersistentDataType<T, Z> {
    private final Plugin plugin;
    private final CustomNbtType<T, Z> customNbtType;

    public CustomNbtPersistentDataTypeWrapper(@NotNull Plugin plugin, @NotNull CustomNbtType<T, Z> customNbtType) {
        this.plugin = plugin;
        this.customNbtType = customNbtType;
    }

    @Override
    public @NotNull Class<T> getPrimitiveType() {
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        return customNbtType.getPrimitiveType();
    }

    @Override
    public @NotNull Class<Z> getComplexType() {
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        return customNbtType.getComplexType();
    }

    @Override
    public @NotNull T toPrimitive(@NotNull Z complex, @NotNull PersistentDataAdapterContext context) {
        Plugin plugin = getPlugin();
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        CustomNbtContext convertedContext = convertContext(plugin, context);
        return customNbtType.toPrimitive(complex, convertedContext);
    }

    @Override
    public @NotNull Z fromPrimitive(@NotNull T primitive, @NotNull PersistentDataAdapterContext context) {
        Plugin plugin = getPlugin();
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        CustomNbtContext convertedContext = convertContext(plugin, context);
        return customNbtType.fromPrimitive(primitive, convertedContext);
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    private @NotNull CustomNbtType<T, Z> getCustomNbtType() {
        return this.customNbtType;
    }
}
