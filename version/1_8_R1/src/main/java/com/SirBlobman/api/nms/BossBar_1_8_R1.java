package com.SirBlobman.api.nms;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.inventivetalent.bossbar.BossBarAPI;

import net.md_5.bungee.api.chat.TextComponent;

public class BossBar_1_8_R1 extends BossBar {
    private static final Map<UUID, org.inventivetalent.bossbar.BossBar> BOSS_BARS = new HashMap<>();
    
    private final String title;
    private final double progress;
    private final org.inventivetalent.bossbar.BossBarAPI.Color color;
    private final org.inventivetalent.bossbar.BossBarAPI.Style style;
    public BossBar_1_8_R1(String title, double progress, BossBarAPI.Color color, BossBarAPI.Style style) {
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
    public BossBarAPI.Color getColor() {
        return this.color;
    }
    
    @Override
    public BossBarAPI.Style getStyle() {
        return this.style;
    }
    
    @Override
    public void sendTo(Player player) {
        if(player == null) return;
        
        org.inventivetalent.bossbar.BossBar bossBar = BossBarAPI.addBar(player, new TextComponent(this.title), getColor(), getStyle(), (float) this.progress);
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
        org.inventivetalent.bossbar.BossBar  bossBar = BOSS_BARS.getOrDefault(uuid, null);
        if(bossBar == null) return;
        
        bossBar.setVisible(false);
        bossBar.removePlayer(player);
        BOSS_BARS.remove(uuid);
    }
}
