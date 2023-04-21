package com.github.sirblobman.api.nms;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;

public final class PlayerHandler_1_12_R1 extends PlayerHandler {
    public PlayerHandler_1_12_R1(@NotNull JavaPlugin plugin) {
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
        if (!(player instanceof CraftPlayer)) {
            throw new IllegalArgumentException("player must be a valid bukkit player.");
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        return nmsPlayer.getAbsorptionHearts();
    }

    @Override
    public void setAbsorptionHearts(@NotNull Player player, double hearts) {
        if (!(player instanceof CraftPlayer)) {
            throw new IllegalArgumentException("player must be a valid bukkit player.");
        }

        CraftPlayer craftPlayer = (CraftPlayer) player;
        EntityPlayer nmsPlayer = craftPlayer.getHandle();
        nmsPlayer.setAbsorptionHearts((float) hearts);
    }

    @Override
    public void sendCooldownPacket(@NotNull Player player, @NotNull Material material, int ticksLeft) {
        player.setCooldown(material, ticksLeft);
    }
}
