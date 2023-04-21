package com.github.sirblobman.api.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import org.bukkit.plugin.Plugin;

public final class WrapperPluginResourceHolder implements IResourceHolder {
    private final Plugin plugin;

    public WrapperPluginResourceHolder(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getName() {
        Plugin plugin = getPlugin();
        return plugin.getName();
    }

    @Override
    public @NotNull String getKeyName() {
        String pluginName = getName();
        return pluginName.toLowerCase(Locale.US);
    }

    @Override
    public @NotNull File getDataFolder() {
        Plugin plugin = getPlugin();
        return plugin.getDataFolder();
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String name) {
        Plugin plugin = getPlugin();
        return plugin.getResource(name);
    }

    @Override
    public @NotNull Logger getLogger() {
        Plugin plugin = getPlugin();
        return plugin.getLogger();
    }

    public @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
