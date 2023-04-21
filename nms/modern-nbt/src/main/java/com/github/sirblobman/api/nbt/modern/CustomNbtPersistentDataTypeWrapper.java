package com.github.sirblobman.api.nbt.modern;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

import static com.github.sirblobman.api.nbt.modern.PersistentDataConverter.convertContext;

public final class CustomNbtPersistentDataTypeWrapper<T, Z> implements PersistentDataType<T, Z> {
    private final Plugin plugin;
    private final CustomNbtType<T, Z> customNbtType;

    public CustomNbtPersistentDataTypeWrapper(Plugin plugin, CustomNbtType<T, Z> customNbtType) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.customNbtType = Validate.notNull(customNbtType, "customNbtType must not be null!");
    }

    @NotNull
    @Override
    public Class<T> getPrimitiveType() {
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        return customNbtType.getPrimitiveType();
    }

    @NotNull
    @Override
    public Class<Z> getComplexType() {
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        return customNbtType.getComplexType();
    }

    @NotNull
    @Override
    public T toPrimitive(@NotNull Z complex, @NotNull PersistentDataAdapterContext context) {
        Plugin plugin = getPlugin();
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        CustomNbtContext convertedContext = convertContext(plugin, context);
        return customNbtType.toPrimitive(complex, convertedContext);
    }

    @NotNull
    @Override
    public Z fromPrimitive(@NotNull T primitive, @NotNull PersistentDataAdapterContext context) {
        Plugin plugin = getPlugin();
        CustomNbtType<T, Z> customNbtType = getCustomNbtType();
        CustomNbtContext convertedContext = convertContext(plugin, context);
        return customNbtType.fromPrimitive(primitive, convertedContext);
    }

    private Plugin getPlugin() {
        return this.plugin;
    }

    private CustomNbtType<T, Z> getCustomNbtType() {
        return this.customNbtType;
    }
}
