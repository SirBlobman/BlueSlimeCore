package com.SirBlobman.api.nms.bossbar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.SirBlobman.api.nms.Handler;

public abstract class BossBarHandler extends Handler {
    public BossBarHandler(JavaPlugin plugin) {
        super(plugin);
    }

    public abstract BossBarWrapper getBossBar(Player player);
    public abstract void removeBossBar(Player player);
    public abstract void updateBossBar(Player player, String message, double progress, String color, String style);
}