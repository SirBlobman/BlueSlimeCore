package com.github.sirblobman.api.nms;

import org.jetbrains.annotations.NotNull;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Abstract NMS Player Handler Class
 * @author SirBlobman
 */
public abstract class PlayerHandler extends Handler {
    /**
     * Create a new instance of this handler.
     * @param plugin The plugin that owns this instance.
     */
    public PlayerHandler(@NotNull JavaPlugin plugin) {
        super(plugin);
    }

    /**
     * Force a player to respawn.
     * This method can be used to skip the "Respawn or Title Screen" menu.
     * @param player the player that will be respawned.
     */
    public abstract void forceRespawn(@NotNull Player player);

    /**
     * Get the amount of yellow absorption health for a player.
     * The name is misleading, the value returned is in health.
     * @param player the player that will be checked.
     * @return the amount of yellow absorption health for a player.
     */
    public abstract double getAbsorptionHearts(@NotNull Player player);

    /**
     * Set the amount of yellow absorption health for a player.
     * The name is misleading, the value is in health.
     * @param player the player that will be changed.
     * @param hearts the amount of yellow absorption health to set.
     */
    public abstract void setAbsorptionHearts(@NotNull Player player, double hearts);

    /**
     * Send an item cooldown packet to a player.
     * The cooldown displays on an item the amount of time left before the player can use that type of item again.
     * @param player The player that will receive the packet.
     * @param material The type of item that will be on cooldown.
     * @param ticksLeft The amount of ticks left before the item can be used again.
     */
    public abstract void sendCooldownPacket(@NotNull Player player, @NotNull Material material, int ticksLeft);
}
