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
        // Do Nothing
    }
    
    @Override
    public Style getStyle() {
        return Style.PROGRESS;
    }
    
    @Override
    public void setStyle(String style) {
        // Do Nothing
    }
    
    @Override
    public void removeFlag(String flag) {
        // Do Nothing
    }
    
    @Override
    public void addFlag(String flag) {
        // Do Nothing
    }
    
    @Override
    public boolean hasFlag(String flag) {
        return false;
    }
    
    @Override
    public void setProgress(double progress) {
        Player player = getPlayer();
        if(player == null) return;
        
        float floatProgress = (float) progress;
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
        // Do Nothing
    }
    
    @Override
    public void removePlayer(Player player) {
        // Do Nothing
    }
    
    @Override
    public List<Player> getPlayers() {
        Player player = getPlayer();
        return (player == null ? Collections.emptyList() : Collections.singletonList(player));
    }
    
    @Override
    public void setVisible(boolean visible) {
        // Do Nothing
    }
    
    @Override
    public boolean isVisible() {
        Player player = getPlayer();
        return (player != null);
    }
}