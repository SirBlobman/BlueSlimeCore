package com.github.sirblobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class ServerHandler extends Handler {
    public ServerHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract int getMaxWorldSize();
}
