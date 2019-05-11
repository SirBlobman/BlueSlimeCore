package com.SirBlobman.api.nms;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class BossBar {
    protected static Map<UUID, BossBar> CURRENT_BOSS_BAR = new HashMap<>();
    
    public abstract String getTitle();
    public abstract double getProgress();
    public abstract Object getColor();
    public abstract Object getStyle();
    public abstract void sendTo(Player player);
    public abstract void remove(Player player);
    
    public static void removeBossBar(Player player) {
        if(player == null) return;
        
        UUID uuid = player.getUniqueId();
        BossBar bossBar = CURRENT_BOSS_BAR.getOrDefault(uuid, null);
        if(bossBar == null) return;
        
        bossBar.remove(player);
    }
    
    public static void setCurrentBossBar(Player player, BossBar bossBar) {
        if(player == null || bossBar == null) return;
        
        UUID uuid = player.getUniqueId();
        CURRENT_BOSS_BAR.put(uuid, bossBar);
    }
}