package com.github.sirblobman.api.nbt.modern;

import org.jetbrains.annotations.NotNull;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContext;

import static com.github.sirblobman.api.nbt.modern.PersistentDataConverter.convertContainer;

public final class CustomNbtPersistentDataAdapterContextWrapper implements CustomNbtContext {
    private final Plugin plugin;
    private final PersistentDataAdapterContext context;

    public CustomNbtPersistentDataAdapterContextWrapper(@NotNull Plugin plugin,
                                                        @NotNull PersistentDataAdapterContext context) {
        this.plugin = plugin;
        this.context = context;
    }

    @Override
    public @NotNull CustomNbtContainer newCustomNbtContainer() {
        Plugin plugin = getPlugin();
        PersistentDataAdapterContext context = getContext();
        PersistentDataContainer persistentDataContainer = context.newPersistentDataContainer();
        return convertContainer(plugin, persistentDataContainer);
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    private @NotNull PersistentDataAdapterContext getContext() {
        return this.context;
    }
}
