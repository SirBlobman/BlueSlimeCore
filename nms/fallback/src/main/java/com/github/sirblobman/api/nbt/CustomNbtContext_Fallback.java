package com.github.sirblobman.api.nbt;

import org.jetbrains.annotations.NotNull;

import org.bukkit.plugin.Plugin;

public final class CustomNbtContext_Fallback implements CustomNbtContext {
    private final Plugin plugin;

    public CustomNbtContext_Fallback(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }

    @Override
    public @NotNull CustomNbtContainer newCustomNbtContainer() {
        Plugin plugin = getPlugin();
        return new CustomNbtContainer_Fallback(plugin);
    }
}
