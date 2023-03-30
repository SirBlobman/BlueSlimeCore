package com.github.sirblobman.api.nms;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PropertyManager;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;

public final class ServerHandler_1_7_R4 extends ServerHandler {
    public ServerHandler_1_7_R4(JavaPlugin plugin) {
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
        // CraftBukkit 1.7.10 does not seem to have a method for TPS.
        return new double[]{20.0D, 20.0D, 20.0D};
    }
}
