package com.github.sirblobman.api.nms;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.VersionUtility;

public final class ServerHandler_Fallback extends ServerHandler {
    public ServerHandler_Fallback(@NotNull JavaPlugin plugin) {
        super(plugin);

        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        Logger logger = getLogger();
        logger.warning("Using fallback ServerHandler.");
        logger.warning("Version '" + minecraftVersion + "' and NMS '" + nmsVersion + "' combo is not supported.");
        logger.warning("Please contact SirBlobman if you believe this is a mistake.");
        logger.warning("https://github.com/SirBlobman/BlueSlimeCore/issues/new/choose");
    }

    @Override
    public int getMaxWorldSize() {
        // Not supported, assume server.properties default value.
        return 29_999_984;
    }

    @Override
    public double @NotNull [] getServerTpsValues() {
        // Not Supported, assume everything is fine.
        return new double[] {20.0D, 20.0D, 20.0D};
    }

    @Override
    public @NotNull CommandMap getCommandMap() {
        // Not Supported
        throw new UnsupportedOperationException("Fallback handler does not support command map.");
    }
}
