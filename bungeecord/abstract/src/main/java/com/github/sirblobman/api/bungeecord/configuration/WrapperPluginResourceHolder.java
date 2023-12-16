package com.github.sirblobman.api.bungeecord.configuration;

import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

public final class WrapperPluginResourceHolder implements IResourceHolder {
    private final Plugin plugin;

    public WrapperPluginResourceHolder(@NotNull Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull File getDataFolder() {
        Plugin plugin = getPlugin();
        return plugin.getDataFolder();
    }

    @Override
    public @Nullable InputStream getResource(@NotNull String name) {
        Plugin plugin = getPlugin();
        return plugin.getResourceAsStream(name);
    }

    @Override
    public @NotNull Logger getLogger() {
        Plugin plugin = getPlugin();
        return plugin.getLogger();
    }

    private @NotNull Plugin getPlugin() {
        return this.plugin;
    }
}
