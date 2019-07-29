package com.SirBlobman.api.nms;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class BossBar {
	private static final Map<UUID, BossBar> BOSS_BAR_MAP = new HashMap<>();
    
    public static void removeBossBar(Player player) {
        if(player == null) return;
        
        UUID uuid = player.getUniqueId();
        BossBar bossBar = BOSS_BAR_MAP.getOrDefault(uuid, null);
        if(bossBar == null) return;
        
        bossBar.remove();
        BOSS_BAR_MAP.remove(uuid);
    }
    
    public static BossBar getCurrentBossBar(Player player) {
    	if(player == null) return null;
    	
    	UUID uuid = player.getUniqueId();
    	return BOSS_BAR_MAP.getOrDefault(uuid, null);
    }
    
    public static void setCurrentBossBar(Player player, BossBar bossBar) {
        if(player == null || bossBar == null) return;
        
        UUID uuid = player.getUniqueId();
        BOSS_BAR_MAP.put(uuid, bossBar);
    }
    
    
    private UUID uuid;
    private String title, color, style;
    private double progress;
    public BossBar(Player player, String title, double progress, String barColor, String barStyle) {
    	this.uuid = player.getUniqueId();
    	this.progress = progress;
    	this.title = title;
    	this.color = barColor;
    	this.style = barStyle;
    }
    
    public final Player getPlayer() {
    	return Bukkit.getPlayer(this.uuid);
    }
    
    public final void update(double progress, String title) {
    	this.progress = progress;
    	this.title = title;
    	send();
    }
    
    public String getTitle() {
    	return this.title;
    }
    
    public double getProgress() {
    	return this.progress;
    }
    
    public String getBarColorString() {
    	return this.color;
    }
    
    public String getBarStyleString() {
    	return this.style;
    }
    
    public abstract Object getBarColor();
    public abstract Object getBarStyle();
    public abstract void send();
    public abstract void remove();
}