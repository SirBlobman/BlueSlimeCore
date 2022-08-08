package com.github.sirblobman.api.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.utility.Validate;

public final class WrapperPluginResourceHolder implements IResourceHolder {
    private final Plugin plugin;

    public WrapperPluginResourceHolder(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public String getName() {
        Plugin plugin = getPlugin();
        return plugin.getName();
    }

    @Override
    public String getKeyName() {
        String pluginName = getName();
        return pluginName.toLowerCase(Locale.US);
    }

    @Override
    public File getDataFolder() {
        Plugin plugin = getPlugin();
        return plugin.getDataFolder();
    }

    @Override
    public InputStream getResource(String name) {
        Plugin plugin = getPlugin();
        return plugin.getResource(name);
    }

    @Override
    public Logger getLogger() {
        Plugin plugin = getPlugin();
        return plugin.getLogger();
    }

    public Plugin getPlugin() {
        return this.plugin;
    }
}
