package com.github.sirblobman.api.nms;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PropertyManager;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;

public final class ServerHandler_1_12_R1 extends ServerHandler {
    public ServerHandler_1_12_R1(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        Server server = getServer();
        int defaultValue = 29_999_984;
        if (!(server instanceof CraftServer)) {
            return defaultValue;
        }

        CraftServer craftServer = (CraftServer) server;
        MinecraftServer nmsServer = craftServer.getServer();
        PropertyManager propertyManager = nmsServer.getPropertyManager();
        return propertyManager.getInt("max-world-size", defaultValue);
    }

    @Override
    public double @NotNull [] getServerTpsValues() {
        Server server = getServer();
        if (!(server instanceof CraftServer)) {
            return new double[] {20.0D, 20.0D, 20.0D};
        }

        CraftServer craftServer = (CraftServer) server;
        MinecraftServer nmsServer = craftServer.getServer();
        return Arrays.copyOf(nmsServer.recentTps, 3);
    }
}
