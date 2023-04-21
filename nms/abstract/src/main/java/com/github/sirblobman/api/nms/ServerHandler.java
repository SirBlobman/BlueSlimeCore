package com.github.sirblobman.api.nms;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract NMS Server Handler
 * @author SirBlobman
 */
public abstract class ServerHandler extends Handler {
    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    public ServerHandler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * @return The server instance.
     */
    public final Server getServer() {
        JavaPlugin plugin = getPlugin();
        return plugin.getServer();
    }

    /**
     * @return The server TPS value for 1m
     */
    public double getServerTps1m() {
        double[] serverTpsValues = getServerTpsValues();
        return serverTpsValues[0];
    }

    /**
     * @return The radius for the maximum world size of the server.
     */
    public abstract int getMaxWorldSize();

    /**
     * @return An array that contains the three server TPS values of [1m, 5m, 15m]
     */
    public abstract double @NotNull [] getServerTpsValues();
}
