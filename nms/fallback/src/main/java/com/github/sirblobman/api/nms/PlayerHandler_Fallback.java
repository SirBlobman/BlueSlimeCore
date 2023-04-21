package com.github.sirblobman.api.nms;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.sirblobman.api.utility.VersionUtility;

import org.jetbrains.annotations.NotNull;


public final class PlayerHandler_Fallback extends PlayerHandler {
    public PlayerHandler_Fallback(JavaPlugin plugin) {
        super(plugin);

        String minecraftVersion = VersionUtility.getMinecraftVersion();
        String nmsVersion = VersionUtility.getNetMinecraftServerVersion();

        Logger logger = getLogger();
        logger.warning("Using fallback PlayerHandler.");
        logger.warning("Version '" + minecraftVersion + "' and NMS '" + nmsVersion + "' combo is not supported.");
        logger.warning("Please contact SirBlobman if you believe this is a mistake.");
        logger.warning("https://github.com/SirBlobman/BlueSlimeCore/issues/new/choose");
    }

    @Override
    public void forceRespawn(@NotNull Player player) {
        if (!player.isDead()) {
            return;
        }

        Spigot spigot = player.spigot();
        spigot.respawn();
    }

    @Override
    public double getAbsorptionHearts(@NotNull Player player) {
        return 0.0D;
    }

    @Override
    public void setAbsorptionHearts(@NotNull Player player, double hearts) {
        // Not Supported
    }

    @Override
    public void sendCooldownPacket(@NotNull Player player, @NotNull Material material, int ticksLeft) {
        // Not Supported
    }
}
