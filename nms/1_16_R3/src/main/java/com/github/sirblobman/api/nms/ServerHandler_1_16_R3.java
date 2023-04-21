package com.github.sirblobman.api.nms;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_16_R3.MinecraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;

public final class ServerHandler_1_16_R3 extends ServerHandler {
    public ServerHandler_1_16_R3(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public int getMaxWorldSize() {
        Server server = getServer();
        return server.getMaxWorldSize();
    }

    @Override
    public double @NotNull [] getServerTpsValues() {
        Server server = getServer();
        if (!(server instanceof CraftServer)) {
            return new double[] {20.0D, 20.0D, 20.0D};
        }

        CraftServer craftServer = (CraftServer) server;
        MinecraftServer minecraftServer = craftServer.getServer();
        return Arrays.copyOf(minecraftServer.recentTps, 3);
    }
}
