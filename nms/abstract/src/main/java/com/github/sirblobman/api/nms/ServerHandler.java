package com.github.sirblobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ServerHandler extends Handler {
    public ServerHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract int getMaxWorldSize();

    /**
     * @return The server TPS values [1m, 5m, 15m]
     */
    public abstract double[] getServerTpsValues();

    /**
     * @return The server TPS value for 1m
     */
    public double getServerTps1m() {
        double[] serverTpsValues = getServerTpsValues();
        return serverTpsValues[0];
    }
}
