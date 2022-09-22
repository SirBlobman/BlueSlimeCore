package com.github.sirblobman.api.nms;

import org.bukkit.plugin.java.JavaPlugin;

public final class ServerHandler_Fallback extends ServerHandler {
    public ServerHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        return 29_999_984;
    }
}
