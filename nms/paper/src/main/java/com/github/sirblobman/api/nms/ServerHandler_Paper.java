package com.github.sirblobman.api.nms;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerHandler_Paper extends ServerHandler {
    public ServerHandler_Paper(@NotNull JavaPlugin plugin) {
        super(plugin);
        Logger logger = getLogger();
        logger.info("Using non-NMS Paper ServerHandler");
    }

    @Override
    public int getMaxWorldSize() {
        Server server = getServer();
        return server.getMaxWorldSize();
    }

    @Override
    public double @NotNull [] getServerTpsValues() {
        Server server = getServer();
        return server.getTPS();
    }
}
