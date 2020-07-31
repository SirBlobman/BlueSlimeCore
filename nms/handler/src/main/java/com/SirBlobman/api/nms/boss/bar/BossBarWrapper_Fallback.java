package com.SirBlobman.api.nms.boss.bar;

import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;

public class BossBarWrapper_Fallback extends BossBarWrapper {
    public BossBarWrapper_Fallback(Player player) {
        super(player);
    }
    
    @Override
    public String getTitle() {
        return "N/A";
    }
    
    @Override
    public void setTitle(String title) {
        Player player = getPlayer();
        if(player == null) return;
        
        player.sendMessage("[Boss Bar] " + title);
    }
    
    @Override
    public String getColor() {
        return "N/A";
    }
    
    @Override
    public void setColor(String color) {
        // Do Nothing
    }
    
    @Override
    public String getStyle() {
        return "N/A";
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
        // Do Nothing
    }
    
    @Override
    public double getProgress() {
        return 0.0D;
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
        return true;
    }
}