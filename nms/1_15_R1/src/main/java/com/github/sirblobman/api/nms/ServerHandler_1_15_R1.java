package com.github.sirblobman.api.nms;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_15_R1.DedicatedServer;
import net.minecraft.server.v1_15_R1.DedicatedServerProperties;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;

public final class ServerHandler_1_15_R1 extends ServerHandler {
    public ServerHandler_1_15_R1(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        Server server = Bukkit.getServer();
        if (!(server instanceof CraftServer)) {
            return 29_999_984;
        }

        CraftServer craftServer = (CraftServer) server;
        DedicatedServer dedicatedServer = craftServer.getServer();
        DedicatedServerProperties properties = dedicatedServer.getDedicatedServerProperties();
        return properties.maxWorldSize;
    }

    @Override
    public double[] getServerTpsValues() {
        Server server = Bukkit.getServer();
        if (!(server instanceof CraftServer)) {
            return new double[] {20.0D, 20.0D, 20.0D};
        }

        CraftServer craftServer = (CraftServer) server;
        MinecraftServer minecraftServer = craftServer.getServer();
        return Arrays.copyOf(minecraftServer.recentTps, 3);
    }
}
