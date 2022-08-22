package com.github.sirblobman.api.nms;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlayerHandler_Fallback extends PlayerHandler {
    public PlayerHandler_Fallback(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sendActionBar(Player player, String message) {
        // Do Nothing
    }

    @Override
    public void sendTabInfo(Player player, String header, String footer) {
        // Do Nothing
    }

    @Override
    public void forceRespawn(Player player) {
        // Do Nothing
    }

    @Override
    public double getAbsorptionHearts(Player player) {
        return 0.0D;
    }

    @Override
    public void setAbsorptionHearts(Player player, double hearts) {
        // Do Nothing
    }

    @Override
    public void sendCooldownPacket(Player player, Material material, int ticksLeft) {
        // Do Nothing
    }
}
