package com.github.sirblobman.api.nbt.modern;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.nbt.CustomNbtContainer;
import com.github.sirblobman.api.nbt.CustomNbtContext;
import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

import static com.github.sirblobman.api.nbt.modern.PersistentDataConverter.convertContainer;

public final class CustomNbtPersistentDataAdapterContextWrapper implements CustomNbtContext {
    private final Plugin plugin;
    private final PersistentDataAdapterContext context;

    public CustomNbtPersistentDataAdapterContextWrapper(Plugin plugin, PersistentDataAdapterContext context) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
        this.context = Validate.notNull(context, "context must not be null!");
    }

    @Override
    public @NotNull CustomNbtContainer newCustomNbtContainer() {
        Plugin plugin = getPlugin();
        PersistentDataAdapterContext context = getContext();
        PersistentDataContainer persistentDataContainer = context.newPersistentDataContainer();
        return convertContainer(plugin, persistentDataContainer);
    }

    private Plugin getPlugin() {
        return this.plugin;
    }

    private PersistentDataAdapterContext getContext() {
        return this.context;
    }
}
