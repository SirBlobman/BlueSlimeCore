package com.github.sirblobman.api.bungeecord.configuration;

import org.jetbrains.annotations.NotNull;

import net.md_5.bungee.api.plugin.Plugin;

public abstract class ConfigurablePlugin extends Plugin {
    private final ConfigurationManager configurationManager;

    public ConfigurablePlugin() {
        this.configurationManager = new ConfigurationManager(this);
    }

    public @NotNull ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    @Override
    public abstract void onLoad();

    @Override
    public abstract void onEnable();

    @Override
    public abstract void onDisable();
}
