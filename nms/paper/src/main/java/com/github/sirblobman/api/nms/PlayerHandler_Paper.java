package com.github.sirblobman.api.nms;

import java.util.logging.Logger;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Player.Spigot;
import org.bukkit.plugin.java.JavaPlugin;


public final class PlayerHandler_Paper extends PlayerHandler {
    public PlayerHandler_Paper(@NotNull JavaPlugin plugin) {
        super(plugin);
        Logger logger = getLogger();
        logger.info("Using non-NMS Paper PlayerHandler");
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
        return player.getAbsorptionAmount();
    }

    @Override
    public void setAbsorptionHearts(@NotNull Player player, double hearts) {
        player.setAbsorptionAmount(hearts);
    }

    @Override
    public void sendCooldownPacket(@NotNull Player player, @NotNull Material material, int ticksLeft) {
        player.setCooldown(material, ticksLeft);
    }
}
