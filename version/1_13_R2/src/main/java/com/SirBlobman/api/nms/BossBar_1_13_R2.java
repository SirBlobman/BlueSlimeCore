package com.SirBlobman.api.nms;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBar_1_13_R2 extends BossBar {
    private static final Map<UUID, org.bukkit.boss.BossBar> BOSS_BARS = new HashMap<>();
    
    private final String title;
    private final double progress;
    private final BarColor color;
    private final BarStyle style;
    public BossBar_1_13_R2(String title, double progress, BarColor color, BarStyle style) {
        this.title = title;
        this.progress = progress;
        this.color = color;
        this.style = style;
    }
    
    @Override
    public String getTitle() {
        String color = ChatColor.translateAlternateColorCodes('&', this.title);
        return color;
    }
    
    @Override
    public double getProgress() {
        return this.progress;
    }
    
    @Override
    public BarColor getColor() {
        return this.color;
    }
    
    @Override
    public BarStyle getStyle() {
        return this.style;
    }
    
    @Override
    public void sendTo(Player player) {
        if(player == null) return;
        
        org.bukkit.boss.BossBar bossBar = Bukkit.createBossBar(getTitle(), getColor(), getStyle());
        bossBar.setProgress(getProgress());
        bossBar.addPlayer(player);
        bossBar.setVisible(true);
        
        UUID uuid = player.getUniqueId();
        BOSS_BARS.put(uuid, bossBar);
        setCurrentBossBar(player, this);
    }
    
    @Override
    public void remove(Player player) {
        removeBar(player);
    }
    
    public static void removeBar(Player player) {
        if(player == null) return;
        
        UUID uuid = player.getUniqueId();
        org.bukkit.boss.BossBar bossBar = BOSS_BARS.getOrDefault(uuid, null);
        if(bossBar == null) return;
        
        bossBar.setVisible(false);
        bossBar.removePlayer(player);
        BOSS_BARS.remove(uuid);
    }
}
