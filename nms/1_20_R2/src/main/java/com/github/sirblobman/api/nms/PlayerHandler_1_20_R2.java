package com.github.sirblobman.api.nms;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHandler_1_20_R2 extends PlayerHandler {
    public PlayerHandler_1_20_R2(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void forceRespawn(@NotNull Player player) {
        if (!player.isDead()) {
            return;
        }

        Player.Spigot spigot = player.spigot();
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
