package com.github.sirblobman.api.nms;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Server;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.MinecraftServer;
import org.bukkit.craftbukkit.CraftServer;

public final class ServerHandler_1_21_R2 extends ServerHandler {
    public ServerHandler_1_21_R2(JavaPlugin plugin) {
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
        if (!(server instanceof CraftServer craftServer)) {
            return new double[] {20.0D, 20.0D, 20.0D};
        }

        MinecraftServer minecraftServer = craftServer.getServer();
        return Arrays.copyOf(minecraftServer.recentTps, 3);
    }

    @Override
    public @NotNull CommandMap getCommandMap() {
        Server server = getServer();
        if (!(server instanceof CraftServer craftServer)) {
            throw new UnsupportedOperationException("Server implementation is not the correct class.");
        }

        return craftServer.getCommandMap();
    }
}
