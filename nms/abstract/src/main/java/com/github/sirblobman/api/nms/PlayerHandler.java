package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PlayerHandler extends Handler {
    public PlayerHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract void forceRespawn(Player player);

    public abstract double getAbsorptionHearts(Player player);

    public abstract void setAbsorptionHearts(Player player, double hearts);

    public abstract void sendCooldownPacket(Player player, Material material, int ticksLeft);

}
