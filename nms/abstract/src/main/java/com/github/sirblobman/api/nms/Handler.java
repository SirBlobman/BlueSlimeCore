package com.github.sirblobman.api.nms;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract NMS Handler Class
 * @author SirBlobman
 */
public abstract class Handler {
    private final JavaPlugin plugin;

    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    protected Handler(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @return The plugin that owns this instance.
     */
    public final @NotNull JavaPlugin getPlugin() {
        return this.plugin;
    }

    /**
     * @return The logger from the plugin.
     * @see JavaPlugin#getLogger()
     */
    public final @NotNull Logger getLogger() {
        JavaPlugin plugin = getPlugin();
        return plugin.getLogger();
    }
}
