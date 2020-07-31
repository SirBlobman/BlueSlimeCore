package com.SirBlobman.api.nms.boss.bar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BossBarHandler {
    private final JavaPlugin plugin;
    public BossBarHandler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public final JavaPlugin getPlugin() {
        return this.plugin;
    }
    
    public abstract BossBarWrapper getBossBar(Player player);
    public abstract void removeBossBar(Player player);
    public abstract void updateBossBar(Player player, String message, double progress, String color, String style);
}