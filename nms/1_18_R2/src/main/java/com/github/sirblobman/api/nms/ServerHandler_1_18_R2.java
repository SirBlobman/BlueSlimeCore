package com.github.sirblobman.api.nms;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerHandler_1_18_R2 extends ServerHandler {
    public ServerHandler_1_18_R2(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        Server server = Bukkit.getServer();
        return server.getMaxWorldSize();
    }
}
