package com.github.sirblobman.api.nms;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.PropertyManager;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;

public final class ServerHandler_1_13_R2 extends ServerHandler {
    public ServerHandler_1_13_R2(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        Server server = Bukkit.getServer();
        if (!(server instanceof CraftServer)) {
            return 29_999_984;
        }

        CraftServer craftServer = (CraftServer) server;
        MinecraftServer minecraftServer = craftServer.getServer();
        PropertyManager propertyManager = minecraftServer.getPropertyManager();
        return propertyManager.getInt("max-world-size", 29_999_984);
    }

    @Override
    public double[] getServerTpsValues() {
        Server server = Bukkit.getServer();
        if (!(server instanceof CraftServer)) {
            return new double[]{20.0D, 20.0D, 20.0D};
        }

        CraftServer craftServer = (CraftServer) server;
        MinecraftServer minecraftServer = craftServer.getServer();
        return Arrays.copyOf(minecraftServer.recentTps, 3);
    }
}
