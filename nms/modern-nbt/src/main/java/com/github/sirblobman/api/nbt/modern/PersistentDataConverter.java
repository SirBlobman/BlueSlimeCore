package com.github.sirblobman.api.nbt.modern;

import org.jetbrains.annotations.NotNull;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

public final class PersistentDataConverter {
    public static <T, Z> @NotNull PersistentDataType<T, Z> convertType(@NotNull Plugin plugin,
                                                                       @NotNull CustomNbtType<T, Z> customNbtType) {
        return new CustomNbtPersistentDataTypeWrapper<>(plugin, customNbtType);
    }

    public static @NotNull CustomNbtContext convertContext(@NotNull Plugin plugin,
                                                           @NotNull PersistentDataAdapterContext context) {
        return new CustomNbtPersistentDataAdapterContextWrapper(plugin, context);
    }

    public static @NotNull CustomNbtContainer convertContainer(@NotNull Plugin plugin,
                                                               @NotNull PersistentDataContainer container) {
        return new CustomNbtPersistentDataContainerWrapper(plugin, container);
    }
}
