package com.github.sirblobman.api.nbt.modern;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.nbt.CustomNbtType;

public final class PersistentDataConverter {
    public static <T, Z> PersistentDataType<T, Z> convertType(Plugin plugin, CustomNbtType<T, Z> customNbtType) {
        return new CustomNbtPersistentDataTypeWrapper<>(plugin, customNbtType);
    }

    public static CustomNbtContext convertContext(Plugin plugin, PersistentDataAdapterContext context) {
        return new CustomNbtPersistentDataAdapterContextWrapper(plugin, context);
    }

    public static CustomNbtContainer convertContainer(Plugin plugin, PersistentDataContainer container) {
        return new CustomNbtPersistentDataContainerWrapper(plugin, container);
    }
}
