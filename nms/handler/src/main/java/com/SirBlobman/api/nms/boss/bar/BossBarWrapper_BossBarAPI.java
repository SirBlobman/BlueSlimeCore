package com.SirBlobman.api.nms.boss.bar;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

import org.inventivetalent.bossbar.BossBarAPI;
import org.inventivetalent.bossbar.BossBarAPI.Color;
import org.inventivetalent.bossbar.BossBarAPI.Style;

@SuppressWarnings("deprecation")
public class BossBarWrapper_BossBarAPI extends BossBarWrapper {
    public BossBarWrapper_BossBarAPI(Player player) {
        super(player);
    }
    
    @Override
    public String getTitle() {
        Player player = getPlayer();
        if(player == null) return "N/A";
        
        return BossBarAPI.getMessage(player);
    }
    
    @Override
    public void setTitle(String title) {
        Player player = getPlayer();
        if(player == null) return;
    
        BossBarAPI.setMessage(player, title);
    }
    
    @Override
    public Color getColor() {
        return Color.BLUE;
    }
    
    @Override
    public void setColor(String color) {
        // N/A
    }
    
    @Override
    public Style getStyle() {
        return Style.PROGRESS;
    }
    
    @Override
    public void setStyle(String style) {
        // N/A
    }
    
    @Override
    public void removeFlag(String flag) {
        // N/A
    }
    
    @Override
    public void addFlag(String flag) {
        // N/A
    }
    
    @Override
    public boolean hasFlag(String flag) {
        return false;
    }
    
    @Override
    public void setProgress(double progress) {
        Player player = getPlayer();
        if(player == null) return;
        
        float floatProgress = Double.valueOf(progress).floatValue();
        BossBarAPI.setHealth(player, floatProgress);
    }
    
    @Override
    public double getProgress() {
        Player player = getPlayer();
        if(player == null) return 0.0D;
    
        return BossBarAPI.getHealth(player);
    }
    
    @Override
    public void addPlayer(Player player) {
        // N/A
    }
    
    @Override
    public void removePlayer(Player player) {
        // N/A
    }
    
    @Override
    public List<Player> getPlayers() {
        Player player = getPlayer();
        if(player == null) return Collections.emptyList();
        
        return Collections.singletonList(player);
    }
    
    @Override
    public void setVisible(boolean visible) {
        // N/A
    }
    
    @Override
    public boolean isVisible() {
        return true;
    }
}