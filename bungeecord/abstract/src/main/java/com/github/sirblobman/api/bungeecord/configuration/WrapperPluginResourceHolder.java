package com.github.sirblobman.api.bungeecord.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import net.md_5.bungee.api.plugin.Plugin;

import com.github.sirblobman.api.utility.Validate;

public final class WrapperPluginResourceHolder implements IResourceHolder {
    private final Plugin plugin;

    public WrapperPluginResourceHolder(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public File getDataFolder() {
        Plugin plugin = getPlugin();
        return plugin.getDataFolder();
    }

    @Override
    public InputStream getResource(String name) {
        Plugin plugin = getPlugin();
        return plugin.getResourceAsStream(name);
    }

    @Override
    public Logger getLogger() {
        Plugin plugin = getPlugin();
        return plugin.getLogger();
    }

    private Plugin getPlugin() {
        return this.plugin;
    }
}
