package com.github.sirblobman.api.nbt;

import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.utility.Validate;

import org.jetbrains.annotations.NotNull;

public final class CustomNbtContext_Fallback implements CustomNbtContext {
    private final Plugin plugin;

    public CustomNbtContext_Fallback(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public @NotNull CustomNbtContainer newCustomNbtContainer() {
        Plugin plugin = getPlugin();
        return new CustomNbtContainer_Fallback(plugin);
    }

    private Plugin getPlugin() {
        return this.plugin;
    }
}
