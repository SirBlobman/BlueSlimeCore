package com.github.sirblobman.api.plugin.listener;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PluginListener<JP extends JavaPlugin> implements Listener {
    private final JP plugin;

    public PluginListener(@NotNull JP plugin) {
        this.plugin = plugin;
    }

    public final void register() {
        unregister();

        JP plugin = getPlugin();
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(this, plugin);
    }

    public final void unregister() {
        HandlerList.unregisterAll(this);
    }

    protected final @NotNull JP getPlugin() {
        return this.plugin;
    }

    protected final @NotNull Logger getLogger() {
        JP plugin = getPlugin();
        return plugin.getLogger();
    }
}
