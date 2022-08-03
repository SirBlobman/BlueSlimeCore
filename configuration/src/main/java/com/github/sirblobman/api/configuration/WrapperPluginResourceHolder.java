package com.github.sirblobman.api.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.github.sirblobman.api.utility.Validate;

public final class WrapperPluginResourceHolder implements IResourceHolder {
    private final Plugin plugin;

    public WrapperPluginResourceHolder(Plugin plugin) {
        this.plugin = Validate.notNull(plugin, "plugin must not be null!");
    }

    @Override
    public File getDataFolder() {
        return this.plugin.getDataFolder();
    }

    @Override
    public InputStream getResource(String name) {
        return this.plugin.getResource(name);
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }
}
