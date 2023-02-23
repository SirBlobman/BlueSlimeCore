package com.github.sirblobman.api.nms;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;

public final class ServerHandler_1_18_R2 extends ServerHandler {
    public ServerHandler_1_18_R2(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        Server server = Bukkit.getServer();
        return server.getMaxWorldSize();
    }

    @Override
    public double[] getServerTpsValues() {
        Server server = Bukkit.getServer();
        if (!(server instanceof CraftServer craftServer)) {
            return new double[] {20.0D, 20.0D, 20.0D};
        }

        MinecraftServer minecraftServer = craftServer.getServer();
        return Arrays.copyOf(minecraftServer.recentTps, 3);
    }
}
